Recipe Wizard
The Recipe Wizard app is designed to help people who don't know what to cook. With this app, they can just look into their fridge, pick a couple of common key ingredients and get some tasty new recipes. Not only are the recipes filtered by ingredients, the app also filters by allergies such as gluten or dairy products as well as diet such as vegan and vegetarian. When ready to cook the recipe, the app goes into cooking mode where the user can look at each instruction one at a time with ingredients and equipment used for each step. This mode is useful when you have dirty hands since you can just wave in front of the app to make it flip through the instructions.

To run the CookingMode, you will need three extra libraries.

    1. jniLibs. Download and put it into your RecipeWizard/app/src/main/ folder.
        https://drive.google.com/drive/folders/0B4sg5SQZxeUtcHNWT2JVd01vR1U?usp=sharing

    2. openCVLibrary310. Download and put it into the RecipeWizard folder.
        https://drive.google.com/drive/folders/0B4sg5SQZxeUtd3M0eHlpeTlaYzA?usp=sharing

    3. touchfreelibrary. Download and put it into the RecipeWizard/app/src/main/java folder.
        https://drive.google.com/drive/folders/0B4sg5SQZxeUtd3R2Z3ZjeEx1cUk?usp=sharing

Make sure the minSdkVersion in build.gradle (Module: app) file is 23.
You also need to add this line to your build.gradle (Module: app) file:
    compile project(':openCVLibrary310')
    
And this line to your settings.gradle file:
    include ':openCVLibrary310'
    
Screenshots of use:
https://gyazo.com/72482f1be2ec3b550429b69f48f38976
https://gyazo.com/3c4ba7fd935f95c116e9bf4832260899
