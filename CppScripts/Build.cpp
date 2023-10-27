#include <stdlib.h>
#include <iostream>

using namespace std;

int main() {
	cout << endl << "Building Java" << endl;
	system("cd src & javac com/vojat/Main.java -d ./Build & cd ..");
	cout << "Build completed!" << endl;
	return 0;
}
