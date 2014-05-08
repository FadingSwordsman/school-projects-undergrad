#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include <sys/mman.h>
#include "test_common.h"
#include "common.h"


int get_log_10(int number)
{
	int temp = number, result = 0;
	while(temp > 1)
	{
		temp /= 10;
		result++;
	}
	return result;
}

void print_results(int stride, int ops, int bytes, int time)
{
	printf("%d,", stride);
	printf("%d,", time);
	printf("%d,", ops);
	printf("%d,", bytes);
	printf("%f,", (float)time/(float)ops);
	printf("%f,", (float)bytes/(float)time);
	printf("%f\n", (float)time/(float)bytes);
}

void print_header(char *title)
{
	printf("%s:\nStride,Time,Operations,Bytes,TimePerOp,BytePerMillisecond,MillisecondPerByte\n", title);
}

long int memcpy_with_stride(int fd, int stride, int filesize)
{
	struct timeval start, end;
	int counter, ops, bytes;
	char *mapped;
	char *buffer = malloc(sizeof(char)*filesize);
	mapped = mmap(0, filesize, PROT_READ, MAP_SHARED, fd, 0);
	bytes = 0;
	ops = 0;
	gettimeofday(&start, NULL);
	for(counter = 0; counter + stride < filesize; counter += stride)
	{
		ops++;
		bytes += stride;
		memcpy(buffer+counter, mapped, stride);
	}
	if(counter < filesize)
	{
		ops++;
		bytes += filesize - counter;
		memcpy(buffer + counter, mapped, filesize - counter);
	}
	gettimeofday(&end, NULL);
	print_results(get_log_10(stride), ops, bytes, get_time_diff(start, end));
	free(buffer);
	return get_time_diff(start, end);
}

void run_memcpy_tests(char *filename)
{
	int fd, filesize, x;
	FILE *file;
	print_header("Memcpy");
	for(x = 1; x < 10000; x*=10)
	{
		fd = open(filename, O_RDONLY);
		file = fdopen(fd, "r");
		fseek(file, 0L, SEEK_END);
		filesize = ftell(file);
		fseek(file, 0L, SEEK_SET);
		memcpy_with_stride(fd, x, filesize);
		close(fd);
	}
}

long int fread_with_stride(FILE *fd, int stride, int filesize)
{
	struct timeval start, end;
	int counter, ops, bytes;
	char *buffer = malloc(sizeof(char)*filesize);
	bytes = 0;
	ops = 0;
	gettimeofday(&start, NULL);
	for(counter = 0; counter + stride < filesize; counter += stride)
	{
		ops++;
		bytes += stride;
		fread(buffer + counter, 1, stride, fd);
	}
	if(counter < filesize)
	{
		ops++;
		bytes += filesize - counter;
		fread(buffer + counter, 1, filesize - counter, fd);
	}
	gettimeofday(&end, NULL);
	print_results(stride, ops, bytes, get_time_diff(start, end));
	free(buffer);
	return get_time_diff(start, end);
}

void run_fread_tests(char *filename)
{
	int fd, filesize, x;
	FILE *file;
	print_header("Fread");
	for(x = 1; x < 10000; x*=10)
	{
		fd = open(filename, O_RDONLY);
		file = fdopen(fd, "r");
		fseek(file, 0L, SEEK_END);
		filesize = ftell(file);
		fseek(file, 0L, SEEK_SET);
		fread_with_stride(file, x, filesize);
		close(fd);
	}
}

long int read_with_stride(int fd, int stride, int filesize)
{
	struct timeval start, end;
	int counter, ops, bytes;
	char *buffer = malloc(sizeof(char)*filesize);
	bytes = 0;
	ops = 0;
	gettimeofday(&start, NULL);
	for(counter = 0; counter + stride < filesize; counter += stride)
	{
		ops++;
		bytes += stride;
		read(fd, buffer + counter, stride);
	}
	if(counter < filesize)
	{
		ops++;
		bytes += filesize - counter;
		read(fd, buffer + counter, filesize - counter);
	}
	read_file(read, buffer, stride, counter, filesize, fd);
	gettimeofday(&end, NULL);
	print_results(stride, ops, bytes, get_time_diff(start, end));
	free(buffer);
	return get_time_diff(start, end);
}

void run_read_tests(char *filename)
{
	int fd, filesize, x;
	FILE *file;
	print_header("Read");
	for(x = 1; x < 10000; x*=10)
	{
		fd = open(filename, O_RDONLY);
		file = fdopen(fd, "r");
		fseek(file, 0L, SEEK_END);
		filesize = ftell(file);
		fseek(file, 0L, SEEK_SET);
		read_with_stride(fd, x, filesize);
		close(fd);
	}
}

int main(int argc, char **argv)
{
	if(argc < 1)
	{
		printf("Could not parse filename.\n");
		return 0;
	}

		run_read_tests(argv[1]);
		run_fread_tests(argv[1]);
		run_memcpy_tests(argv[1]);

	return 0;
}
