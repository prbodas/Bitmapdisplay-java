import java.util.*;
/**
 * Write a description of class Test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Test
{
    public static void main (String[] args)
    {
        ArrayList<Character> c = new ArrayList<Character>(146090);
        char[] cc = new char[c.size()];
        for (int i = 0; i<c.size(); i++)
        {
            cc[i] = (char)(c.get(i));
        }
        
    }
}
