#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "common.h"

pthread_mutex_t *switch_mutex;

pthread_t *child_thread;

int *buffer;

void read()
{
	int counter;
	for(counter = 0; counter < ARR_SIZE; counter += STRIDE/sizeof(int))
		buffer[counter]++;
}

void run_thread()
{
	int counter;
	for(counter = 0; counter < NUM_RUNS; counter++)
	{
		pthread_mutex_lock(switch_mutex);
		read();
		pthread_mutex_unlock(switch_mutex);
	}
}

int main(int argc, char **argv)
{
	buffer = (int*)malloc(ARR_SIZE*sizeof(int));
	switch_mutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));

	pthread_mutex_init(switch_mutex, NULL);

	child_thread = (pthread_t*)malloc(sizeof(pthread_t));

	pthread_create(child_thread, NULL, (void*)run_thread, NULL);

	run_thread();

	return 0;
}
