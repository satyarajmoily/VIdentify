# VIdentify
First Acitivity will be the login screen where you will have to login using the google ID.
Once login is successful the application will direct you to the second activity where you have to pick and API.

I have used 4 APIs i.e.

1) Clarifai - This API is used to process the image and identify the concepts in the image given by the user. Specific type
              concepts can be selected i.e. 
              i) General - The concept General will identify anything in general in the image.
              ii) Apparel - Identifies Fashion related items in the image.
              iii) Food - Identifies Food items and different dishes in the image.
              iv) Moderation - Identifies any unwanted items in the image like drugs.
              v) Travel - Identifies Travel related concepts.
              vi) Wedding - Identifies Wedding related concepts like gloom, bride, flowers etc.

The input image to be processed can be clicked by camera or can be picked from the gallery. Once the image is selected, you 
need to select the concepts to be identified. The image will be processed thereafter.

2) Waston's Tone Analzer - This API is used to process the text given the user and the API will return the Emotional tone,
                            Language tone and Social tone in the text.

The input text can be suppiled using the edittext and once the analyze button is clicked the result will appear.

3) Waston's Conversation - This API is used to interact with your commands and do stuffs in a car dashboard. This API can
                           be used in a car dashboard to have a virtual assistant.
                           It can do things like "Play the music", "Turn on the lights", etc
                           I have created a chat screen where you will be interacting with waston's api.

4) Waston's Language Translator - This API is used to translate the text into any available languages in the API.
                                   When you input a text you need to select which language is the input text in and 
                                   which language do you want to translate it to.

The input text can be suppiled using the edittext and once the translate button is clicked the result will appear.

Finally you can use the menu button to navigate to your profile activity where you can see your profile in which you logged  in.
