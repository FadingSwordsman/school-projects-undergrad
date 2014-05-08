#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include "test_common.h"
#include "common.h"

long int read_with_stride(int fd, int stride, int filesize)
{
	struct timeval start, end;
	int counter;
	char *buffer = malloc(sizeof(char)*filesize);
	gettimeofday(&start, NULL);
	read_file(read, buffer, stride, counter, filesize, fd);
	gettimeofday(&end, NULL);
	free(buffer);
	return get_time_diff(start, end);
}

void run_tests(char *filename)
{
	int fd, filesize, x;
	FILE *file;
	printf("Stride,Time\n");
	for(x = 1; x < 10000; x++)
	{
		fd = open(filename, O_RDONLY);
		file = fdopen(fd, "r");
		fseek(file, 0L, SEEK_END);
		filesize = ftell(file);
		fseek(file, 0L, SEEK_SET);
		printf("%d,%ld\n", x, read_with_stride(fd, x, filesize));
		close(fd);
	}
}

int main(int argc, char **argv)
{
	printf("%s\n", argv[1]);
	if(argc < 1)
	{
		printf("Could not parse filename.\n");
		return 0;
	}

	run_tests(argv[1]);

	return 0;
}
