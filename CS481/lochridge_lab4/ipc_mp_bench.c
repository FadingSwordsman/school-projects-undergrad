#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <time.h>
#include "common.h"

int *to_child;
int *to_parent;
int *buffer;
long int bufferTime;

void read_process(int *pipe_ids)
{
	int x;
	struct timeval start, end;
	gettimeofday(&start, NULL);
	for(x = 0; x < ARR_SIZE; x+= (STRIDE<<3))
		read(pipe_ids[0], buffer + x, STRIDE);
	gettimeofday(&end, NULL);
	bufferTime += get_time_diff(start, end);
}

void write_process(int *pipe_ids)
{
	int x;
	for(x = 0; x < ARR_SIZE; x+= (STRIDE<<3))
		write(pipe_ids[1], buffer + x, STRIDE);
}

void parent()
{
	int count;
	for(count = 0; count < ARR_SIZE; count++)
		buffer[count] = count;
	for(count = 0; count < NUM_RUNS; count++)
	{
		write_process(to_child);
		read_process(to_parent);
	}
}

void child()
{
	int count;
	for(count = 0; count < NUM_RUNS; count++)
	{
		read_process(to_child);
		write_process(to_parent);
	}
}

int main(int argc, char **argv)
{
	struct timeval startTime, endTime;
	bufferTime = 0;
	buffer = (int*) malloc(ARR_SIZE*sizeof(int));
	to_child = (int*)malloc(2*sizeof(int));
	to_parent = (int*)malloc(2*sizeof(int));
	pipe(to_child);
	pipe(to_parent);
	gettimeofday(&startTime, NULL);
	if(fork())
	{
		parent();
		gettimeofday(&endTime, NULL);
		printf("Total time: %ld\n", get_time_diff(startTime, endTime));
		printf("Time reading buffer (parent): %ld\n", bufferTime);
	}
	else
	{
		child();
		gettimeofday(&endTime, NULL);
		printf("Total time: %ld\n", get_time_diff(startTime, endTime));
		printf("Time reading buffer (child): %ld\n", bufferTime);
	}
	free(to_parent);
	free(to_child);
	return 0;
}
