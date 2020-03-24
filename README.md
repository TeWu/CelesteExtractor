Celeste Extractor ![yummy_strawberry](https://i.imgur.com/ev4SKDS.png)
=======

Graphics in the game **[Celeste][0]** are stored in `.data` files. **Celeste Extractor** is a simple tool for losslessly converting `.data` files to easily-readable `.png` files. The goal was to create a tool that is:

  * Extremely simple to use (just double click it)
  * Portable across operating systems (written in Java)
  * Tiny (11KB), yet
  * Very fast (converts all of Celeste's `.data` files in just 1 minute)


Requirements
=======

  * **Java SE Runtime Environment 8**
    * [Download installer][2]


How to use it (simple version)
=======

Step 0
---

Download the newest release of **Celeste Extractor** (`CelesteExtractor_vX.X.X.zip` file) from the [releases][3] page.

Step 1
---

Celeste's `.data` graphics files are stored in `Atlases` directory. Locate that folder on your disk. It might be under the following paths:

```
On Windows:
  C:\Program Files (x86)\Epic Games\Celeste\Content\Graphics\Atlases
  C:\Program Files (x86)\Steam\steamapps\common\Celeste\Content\Graphics\Atlases

On Linux:
  ~/.local/share/Steam/steamapps/common/Celeste/Content/Graphics/Atlases

On Mac:
  ~/Library/Application Support/Steam/steamapps/common/Celeste/Celeste.app/Contents/MacOS/Content/Graphics/Atlases
  ~/Library/Application Support/Celeste/Content/Graphics/Atlases
```

Extract all the files from the `CelesteExtractor_vX.X.X.zip` into the `Atlases` directory.

Step 2
---

To convert all the game's `.data` files to `.png`s:

  * **On Windows** - Double click `CelesteExtractor.bat`
  * **On Linux** - Run `CelesteExtractor.sh`
  * **On Mac** - Run `CelesteExtractor.sh`

After conversion is done, `.png` files will be located in `Atlases/CelesteGraphics` directory.


How to use it (tech-savvy version)
=======

`CelesteExtractor.jar` is a Java CLI application. You can learn all about how to use it by running it with `--help` option:

```
Celeste Extractor  vX.X.X
Converts .data files to .png losslessly

Usage:
    java -jar CelesteExtractor.jar [OPTION] [INPUT_DIR] [OUTPUT_DIR]
            Converts all .data files in INPUT_DIR and its subdirectories and puts resulting .png
            files in a matching directory structure rooted at OUTPUT_DIR. Both INPUT_DIR and
            OUTPUT_DIR arguments are optional. If not specified, OUTPUT_DIR defaults to
            'CelesteGraphics', and INPUT_DIR defaults to current working directory.
            If both INPUT_DIR and OUTPUT_DIR are not specified, program prompts for confirmation
            before starting the conversion.

    java -jar CelesteExtractor.jar [OPTION] INPUT_FILE [OUTPUT_FILE]
            Converts INPUT_FILE (.data file) to OUTPUT_FILE (.png file).
            OUTPUT_FILE argument is optional, and when not specified defaults to the value
            of INPUT_FILE argument with '.png' appended at the end.


Options:
    --help      Print this help message
    --version   Print basic information about this program


Example:
    java -jar CelesteExtractor.jar "C:\Program Files (x86)\Steam\steamapps\common\Celeste\Content\Graphics\Atlases"
```


[0]: http://www.celestegame.com/
[2]: https://www.java.com/download
[3]: https://github.com/TeWu/CelesteExtractor/releases
