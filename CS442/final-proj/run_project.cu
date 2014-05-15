#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include <cuda.h>
#include "common.h"

#define NUM_THREADS 256
#define BLOCK_SIZE
#define NUM_BLOCKS

__device__ int *ciphertext;

int *plaintext;

__global__ void calculate_entropy()
{
	
}

__global__ void run_cipher()
{
	
} 

__global__ setup_gpu()
{
	
}

int main(int argc, char **argv)
{
	int x;
	char* ciphertext;
	setup_gpu();

	ciphertext

	for(x = 0; x < BLOCK_SIZE*NUM_BLOCKS; x += BLOCK_SIZE)
	{
		//Thank you http://on-demand.gputechconf.com/gtc-express/2011/presentations/StreamsAndConcurrencyWebinar.pdf
		run_cipher <<< block, 0, stream2 >>> ();
		
		calculate_entropy <<< block, NUM_THREADS, 0, stream3 >>> ();
	}
}
