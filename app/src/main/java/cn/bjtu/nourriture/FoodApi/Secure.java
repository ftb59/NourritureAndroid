package cn.bjtu.nourriture.FoodApi;

import java.text.StringCharacterIterator;

/**
 * Created by ftb on 15-1-3.
 */
public class Secure {

    public static String forJSON(String aText){
        final StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character = iterator.current();
        while (character != StringCharacterIterator.DONE){
            if( character == '\"' ){
                result.append("\\\"");
            }
            else if(character == '\\'){
                result.append("\\\\");
            }
            else if(character == '/'){
                result.append("\\/");
            }
            else if(character == '\b'){
                result.append("\\b");
            }
            else if(character == '\f'){
                result.append("\\f");
            }
            else if(character == '\n'){
                result.append("\\n");
            }
            else if(character == '\r'){
                result.append("\\r");
            }
            else if(character == '\t'){
                result.append("\\t");
            }
            else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
}
