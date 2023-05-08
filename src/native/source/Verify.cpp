#include <tfhe.h>
#include <tfhe_io.h>
#include <stdio.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>
#include "assert.h"
#include <iostream>
#include <fstream>
#include <unistd.h>
#include <jni.h>
#include <D:\LoginId\demo\src\native\include\com_loginid_cryptodid_protocols_TfheVerifier.h>

using namespace std;
 
void compare_bit(LweSample* result, const LweSample* a, const LweSample* b, const LweSample* lsb_carry, LweSample* tmp, const TFheGateBootstrappingCloudKeySet* bk) {
    bootsXNOR(tmp, a, b, bk);
    bootsMUX(result, tmp, lsb_carry, a, bk);
}

// this function compares two multibit words, and puts the max in result
void minimum(LweSample* tmps,LweSample* result, const LweSample* a, const LweSample* b, const int nb_bits, const TFheGateBootstrappingCloudKeySet* bk) {
    //LweSample* tmps = new_gate_bootstrapping_ciphertext_array(2, bk->params);
    
    //initialize the carry to 0
    bootsCONSTANT(&tmps[0], 0, bk);
    //run the elementary comparator gate n times
    for (int i=0; i<nb_bits; i++) {
        compare_bit(&tmps[0], &a[i], &b[i], &tmps[0], &tmps[1], bk);
    }
    //tmps[0] is the result of the comparaison: 0 if a is larger, 1 if b is larger
    //select the max and copy it to the result
    for (int i=0; i<nb_bits; i++) {
        bootsMUX(&result[i], &tmps[0], &b[i], &a[i], bk);
    }
} 

void Encrypt(int n,LweSample * result, const LweSample* encrypt0,const LweSample *encrypt1,TFheGateBootstrappingCloudKeySet* bk, const TFheGateBootstrappingParameterSet* params)
{
    int i,k[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    /*convert n to binary and store the bits in k*/
    for(i=0;n>0;i++)
    {
        k[i]=n%2;
        n=n/2;
    }

    /* encrypt k*/
    const LweParams *in_out_params = params->in_out_params;
    for (int i=0;i<16;i++){
        if (k[i] == 0){
            lweCopy(&result[i],encrypt0,in_out_params);
        }
        else if (k[i] == 1)
        {
            lweCopy(&result[i],encrypt1,in_out_params);
        }
        else printf("[-] error bits");

    }
}

void verifier_circuit(LweSample * result_for_L,const LweSample* EncK, const LweSample* ciphertext1, const LweSample* ciphertext2,
                      TFheGateBootstrappingCloudKeySet* bk,const TFheGateBootstrappingParameterSet* params)
{   
   LweSample* tmps = new_gate_bootstrapping_ciphertext_array(2, params);
   LweSample* result = new_gate_bootstrapping_ciphertext_array(16, params);
   minimum(tmps,result, ciphertext1, ciphertext2, 16, bk);

   for (int i=0; i<16; i++) {
      bootsAND(&result_for_L[i],&EncK[i],&tmps[0],bk);
   }
   delete_gate_bootstrapping_ciphertext_array(2, tmps);
}

/** generate random k */
int RandomK(int lower, int upper)
{
        return ((rand() %(upper - lower + 1)) + lower);
}

//char* verify(char* verifiableCredentialPath, char* cloudKeyPath, char* publicKeyPath,int minimumRequiredValue)
JNIEXPORT jstring JNICALL Java_com_loginid_cryptodid_protocols_TfheVerifier_verifyAge
  (JNIEnv* env, jobject thisObject)
{
printf("[+]reading the key...\n");

   //reads the cloud key from file
   FILE* cloud_key = fopen("cloud.key","rb");
   TFheGateBootstrappingCloudKeySet* bk = new_tfheGateBootstrappingCloudKeySet_fromFile(cloud_key);
   fclose(cloud_key);
   
   //if necessary, the params are inside the key
   const TFheGateBootstrappingParameterSet* params = bk->params;
   printf("[+]reading the input...\n");

   //reads the 1*16 ciphertexts from claim file
   LweSample* claim = new_gate_bootstrapping_ciphertext_array(16, params);
   FILE* cloud_data = fopen("cloud.data","rb");
   for (int i=0; i<16; i++) 
      import_gate_bootstrapping_ciphertext_fromFile(cloud_data, &claim[i], params);
   fclose(cloud_data);

   /** Generate ZERO and ONE encryption from Public key*/
   LweSample* ONE = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO_R = new_gate_bootstrapping_ciphertext(params);
   srand(time(NULL));
   int r,c=0;
   FILE* PK_pt = fopen("PK.key","rb");
   if(PK_pt != NULL){
      for (int i=0; i<256; i++){
         import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ZERO, params);
         if( i == 0)
               bootsAND(ZERO_R,ZERO,ZERO,bk);  
         r = abs(RandomK(1,256));
         if (r < 150 && c < 128){
               bootsAND(ZERO_R,ZERO,ZERO_R,bk);
               c++;
         }
      }
      import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ONE, params);
      fclose(PK_pt);
   }
   // add 0 + 1 to get new 1 
   bootsOR(ONE,ZERO_R,ONE,bk);
   //printf("[+] c = %d\n",c);
   //printf("[+] read PK passed\n");

   /** encrypt the minimum required value*/
   LweSample* Encrypted_Age = new_gate_bootstrapping_ciphertext_array(16, params);
   Encrypt(21,Encrypted_Age,ZERO_R,ONE,bk,params);

   /** generate random number*/
   int N = abs(RandomK(1000,32767));
   printf("[+] N = %d\n",N);
   LweSample* Encrypted_K = new_gate_bootstrapping_ciphertext_array(16, params);
   /** Encrypt the random number generated*/
   Encrypt(N,Encrypted_K,ZERO_R,ONE,bk,params);

   /***************************************************************************************************/
   printf("[+]doing the homomorphic computation...\n");
   LweSample* L = new_gate_bootstrapping_ciphertext_array(16, params);
   verifier_circuit(L,Encrypted_K,claim,Encrypted_Age,bk,params);

   /***************************************************************************************************/
   
   printf("[+]Export L to file...\n");
   char* AnswerFilePath = (char*) malloc(15 * sizeof(char)); // allocate memory for the string
   strcpy(AnswerFilePath, "Answer.data");
   FILE* L_data = fopen(AnswerFilePath,"wb");
   for (int i=0; i<16; i++) 
      export_gate_bootstrapping_ciphertext_toFile(L_data, &L[i], params);
   fclose(L_data);

   //clean up all pointers
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_Age);
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_K);
   delete_gate_bootstrapping_ciphertext_array(16, claim);

   delete_gate_bootstrapping_ciphertext_array(16, L);
   delete_gate_bootstrapping_cloud_keyset(bk);
   delete_gate_bootstrapping_ciphertext(ONE);
   delete_gate_bootstrapping_ciphertext(ZERO);

   return env->NewStringUTF(AnswerFilePath);
}

JNIEXPORT jstring JNICALL Java_com_loginid_cryptodid_protocols_TfheVerifier_verifyBalance
  (JNIEnv* env, jobject thisObject)
{
printf("[+]reading the key...\n");

   //reads the cloud key from file
   FILE* cloud_key = fopen("cloud.key","rb");
   TFheGateBootstrappingCloudKeySet* bk = new_tfheGateBootstrappingCloudKeySet_fromFile(cloud_key);
   fclose(cloud_key);

   //if necessary, the params are inside the key
   const TFheGateBootstrappingParameterSet* params = bk->params;
   printf("[+]reading the input...\n");

   //reads the 1*16 ciphertexts from claim file
   LweSample* claim = new_gate_bootstrapping_ciphertext_array(16, params);
   FILE* cloud_data = fopen("cloud.data","rb");
   for (int i=0; i<16; i++)
      import_gate_bootstrapping_ciphertext_fromFile(cloud_data, &claim[i], params);
   fclose(cloud_data);

   /** Generate ZERO and ONE encryption from Public key*/
   LweSample* ONE = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO_R = new_gate_bootstrapping_ciphertext(params);
   srand(time(NULL));
   int r,c=0;
   FILE* PK_pt = fopen("PK.key","rb");
   if(PK_pt != NULL){
      for (int i=0; i<256; i++){
         import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ZERO, params);
         if( i == 0)
               bootsAND(ZERO_R,ZERO,ZERO,bk);
         r = abs(RandomK(1,256));
         if (r < 150 && c < 128){
               bootsAND(ZERO_R,ZERO,ZERO_R,bk);
               c++;
         }
      }
      import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ONE, params);
      fclose(PK_pt);
   }
   // add 0 + 1 to get new 1
   bootsOR(ONE,ZERO_R,ONE,bk);
   //printf("[+] c = %d\n",c);
   //printf("[+] read PK passed\n");

   /** encrypt the minimum required value*/
   LweSample* Encrypted_Age = new_gate_bootstrapping_ciphertext_array(16, params);
   Encrypt(100,Encrypted_Age,ZERO_R,ONE,bk,params);

   /** generate random number*/
   int N = abs(RandomK(1000,32767));
   printf("[+] N = %d\n",N);
   LweSample* Encrypted_K = new_gate_bootstrapping_ciphertext_array(16, params);
   /** Encrypt the random number generated*/
   Encrypt(N,Encrypted_K,ZERO_R,ONE,bk,params);

   /***************************************************************************************************/
   printf("[+]doing the homomorphic computation...\n");
   LweSample* L = new_gate_bootstrapping_ciphertext_array(16, params);
   verifier_circuit(L,Encrypted_K,claim,Encrypted_Age,bk,params);

   /***************************************************************************************************/

   printf("[+]Export L to file...\n");
   char* AnswerFilePath = (char*) malloc(15 * sizeof(char)); // allocate memory for the string
   strcpy(AnswerFilePath, "Answer.data");
   FILE* L_data = fopen(AnswerFilePath,"wb");
   for (int i=0; i<16; i++)
      export_gate_bootstrapping_ciphertext_toFile(L_data, &L[i], params);
   fclose(L_data);

   //clean up all pointers
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_Age);
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_K);
   delete_gate_bootstrapping_ciphertext_array(16, claim);

   delete_gate_bootstrapping_ciphertext_array(16, L);
   delete_gate_bootstrapping_cloud_keyset(bk);
   delete_gate_bootstrapping_ciphertext(ONE);
   delete_gate_bootstrapping_ciphertext(ZERO);

   return env->NewStringUTF(AnswerFilePath);
}

JNIEXPORT jstring JNICALL Java_com_loginid_cryptodid_protocols_TfheVerifier_verifyCreditScore
  (JNIEnv* env, jobject thisObject)
{
printf("[+]reading the key...\n");

   //reads the cloud key from file
   FILE* cloud_key = fopen("cloud.key","rb");
   TFheGateBootstrappingCloudKeySet* bk = new_tfheGateBootstrappingCloudKeySet_fromFile(cloud_key);
   fclose(cloud_key);

   //if necessary, the params are inside the key
   const TFheGateBootstrappingParameterSet* params = bk->params;
   printf("[+]reading the input...\n");

   //reads the 1*16 ciphertexts from claim file
   LweSample* claim = new_gate_bootstrapping_ciphertext_array(16, params);
   FILE* cloud_data = fopen("cloud.data","rb");
   for (int i=0; i<16; i++)
      import_gate_bootstrapping_ciphertext_fromFile(cloud_data, &claim[i], params);
   fclose(cloud_data);

   /** Generate ZERO and ONE encryption from Public key*/
   LweSample* ONE = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO = new_gate_bootstrapping_ciphertext(params);
   LweSample* ZERO_R = new_gate_bootstrapping_ciphertext(params);
   srand(time(NULL));
   int r,c=0;
   FILE* PK_pt = fopen("PK.key","rb");
   if(PK_pt != NULL){
      for (int i=0; i<256; i++){
         import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ZERO, params);
         if( i == 0)
               bootsAND(ZERO_R,ZERO,ZERO,bk);
         r = abs(RandomK(1,256));
         if (r < 150 && c < 128){
               bootsAND(ZERO_R,ZERO,ZERO_R,bk);
               c++;
         }
      }
      import_gate_bootstrapping_ciphertext_fromFile(PK_pt, ONE, params);
      fclose(PK_pt);
   }
   // add 0 + 1 to get new 1
   bootsOR(ONE,ZERO_R,ONE,bk);
   //printf("[+] c = %d\n",c);
   //printf("[+] read PK passed\n");

   /** encrypt the minimum required value*/
   LweSample* Encrypted_Age = new_gate_bootstrapping_ciphertext_array(16, params);
   Encrypt(700,Encrypted_Age,ZERO_R,ONE,bk,params);

   /** generate random number*/
   int N = abs(RandomK(1000,32767));
   printf("[+] N = %d\n",N);
   LweSample* Encrypted_K = new_gate_bootstrapping_ciphertext_array(16, params);
   /** Encrypt the random number generated*/
   Encrypt(N,Encrypted_K,ZERO_R,ONE,bk,params);

   /***************************************************************************************************/
   printf("[+]doing the homomorphic computation...\n");
   LweSample* L = new_gate_bootstrapping_ciphertext_array(16, params);
   verifier_circuit(L,Encrypted_K,claim,Encrypted_Age,bk,params);

   /***************************************************************************************************/

   printf("[+]Export L to file...\n");
   char* AnswerFilePath = (char*) malloc(15 * sizeof(char)); // allocate memory for the string
   strcpy(AnswerFilePath, "Answer.data");
   FILE* L_data = fopen(AnswerFilePath,"wb");
   for (int i=0; i<16; i++)
      export_gate_bootstrapping_ciphertext_toFile(L_data, &L[i], params);
   fclose(L_data);
   printf("[+]Export done");

   //clean up all pointers
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_Age);
   delete_gate_bootstrapping_ciphertext_array(16, Encrypted_K);
   delete_gate_bootstrapping_ciphertext_array(16, claim);

   delete_gate_bootstrapping_ciphertext_array(16, L);
   delete_gate_bootstrapping_cloud_keyset(bk);
   delete_gate_bootstrapping_ciphertext(ONE);
   delete_gate_bootstrapping_ciphertext(ZERO);

   return env->NewStringUTF(AnswerFilePath);
}

