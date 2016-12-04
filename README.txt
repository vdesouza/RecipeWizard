To run the CookingMode, you will need three extra folders.

1. jniLibs. Download and put it into your app folder.
    https://drive.google.com/drive/folders/0B4sg5SQZxeUtcHNWT2JVd01vR1U?usp=sharing
    
2. openCVLibrary310. Download and put it into the directory which has the app folder.
    https://drive.google.com/drive/folders/0B4sg5SQZxeUtd3M0eHlpeTlaYzA?usp=sharing
    
3. touchfreelibrary. Download and put it into the java folder.
    https://drive.google.com/drive/folders/0B4sg5SQZxeUtd3R2Z3ZjeEx1cUk?usp=sharing

You also need to add this line to your build.gradle (Module: app) file:
    compile project(':openCVLibrary310')
    
And this line to your settings.gradle file:
    include ':openCVLibrary310'
