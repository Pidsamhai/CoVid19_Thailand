#include <stdio.h>
#include <stdlib.h>

int main()
{
    char ch, md5[31];
    FILE *fp;
    fp = fopen("app-release.md5", "r");
    int i = 0;
    while((ch = fgetc(fp)) != EOF) {
        md5[i] = ch;
        if (i == 31) break;
        i++;
    }
    fclose(fp);
    fp = fopen("checksum.json", "w");
    fprintf(fp,"{\"algorithm\": \"md5\",\"type\":{\"md5\": \"");
    i = 0;
    while (i < 32) {
        fprintf(fp, "%c", md5[i]);
        i++;
    }
    fprintf(fp,"\"}}");
    fclose(fp);
    return 0;
}