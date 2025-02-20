# Инструкция по сборке и запуску программы

1. Для компиляции и запуска программы нужно использовать команду:

   javac -d target src/java/edu/school21/printer/app/App.java src/java/edu/school21/printer/logic/Logic.java

   Это создаст скомпилированные `.class` файлы в папке `target`.

2. Для запуска программы используйте следующую команду:

   java -cp target ex00.ImagesToChar.src.java.edu.school21.printer.app.App"path_to_image.bmp" '.' '0'

   где:
   - "path_to_image.bmp" - полный путь к изображению BMP.
   - '.' - символ для белых пикселей.
   - '0' - символ для черных пикселей.
Пример использования:
   java -cp target ex00.ImagesToChar.src.java.edu.school21.printer.app.App it.bmp '.' '0'
Программа выводит изображение на экран в виде двухцветного ASCII-артистического представления.
