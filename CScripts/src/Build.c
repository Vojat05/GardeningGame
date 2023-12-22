#include <stdio.h>
#include <stdlib.h>

int main() {
	printf("\nBuilding Java!");
	system("cd src & javac com/vojat/Main.java -d ./Build & cd ..");
	printf("\nBuild compleated!\n");
	return 0;
}
