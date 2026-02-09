
import os

root_dir = "src"
output_file = "java_aplikazioko_kodea.md"

with open(output_file, "w", encoding="utf-8") as outfile:
    outfile.write("# Java Aplikazioko Kodea\n\n")
    for subdir, dirs, files in os.walk(root_dir):
        # Sort so output is deterministic
        files.sort()
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(subdir, file)
                outfile.write(f"## {file}\n\n")
                outfile.write("```java\n")
                try:
                    with open(file_path, "r", encoding="utf-8") as infile:
                        outfile.write(infile.read())
                except Exception as e:
                    outfile.write(f"// Error reading file: {e}")
                outfile.write("\n```\n\n")

print(f"Generated {output_file}")
