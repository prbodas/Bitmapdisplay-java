import java.util.*;
import java.io.*;
import gpdraw.*;
import java.awt.Color;
/**
 * This lab took me.... I don't even know..
 * maybe about 7-8 hours total?
 * This lab was very difficult. I still can't 
 * actually believe I cut 5th block to work on 
 * it though... oh well, at least I'm done now. 
 * And it's just band anyway, and I play the tuba,
 * so it's not too bad. I ran into lots of problems,
 * some banal forgetfulness mistakes, and some actual 
 * issues. In the end, I fixed it, obviously, so all
 * is well.
 * Prachi Bodas Period III apcs
 */
public class BitmapUtilPrachiBodasPeriod3
{
    DataInputStream f;
    DrawingTool a;

    // the parts of a bitmap
    public String IDfield; // 2 bytes
    public int size; // 4 bytes, little-endian
    public short appSpec1; // 2 bytes of ap-specific data
    public short appSpec2; // 2 bytes of ap-specific data
    public int offsetToPixData; // 4 bytes, little-endian
    public int bytesinDib; // # of bytes in DIB header (including these 4), 4 bytes, little-endian
    public int width; // 4 bytes, little-endian, positive is left-to-right
    public int height; //4 bytes, little-endian, positive is bottom-to-top
    public short planes; // 2 bytes, num of color planes used
    public short bitsPerPx; // 2 bytes, num of bits in each pixel
    public int BI_RGB; // no compression used 4b
    public int dataInPxArr; // size of data in pixel array + padding 4b
    public int horizRes; //horizontal resolution of image 4b
    public int vertRes; // vertical res of image 4b
    public int colorsInPalette; // 4b, # of colors in palette
    public int impColors; // num of colors that are important, 4b

    public ArrayList<ArrayList> pixelsOfImage = new ArrayList<ArrayList>();

    // tracker variables
    int ind = 0;
    int padding;
    public BitmapUtilPrachiBodasPeriod3()
    {
    }

    public void loadVars()
    {
        IDfield = (char)(readNextBytesInt(1)) +""+ (char)(readNextBytesInt(1)) + "";
        size = readNextBytesIntUnsigned(4);
        appSpec1 = (short)(readNextBytesInt(2));
        appSpec2 = (short)(readNextBytesInt(2));
        offsetToPixData = readNextBytesInt(4);
        bytesinDib = readNextBytesInt(4);
        try{    
            width = Integer.reverseBytes(f.readInt());
        }catch (IOException e){System.out.println("NO");}
        try{    
            height = Integer.reverseBytes(f.readInt());
        }catch (IOException e){System.out.println("NO");}
        planes = (short)(readNextBytesInt(2));
        bitsPerPx = (short)(readNextBytesInt(2));
        BI_RGB = readNextBytesInt(4);
        dataInPxArr = readNextBytesIntUnsigned(4);
        horizRes = readNextBytesInt(4);
        vertRes = readNextBytesInt(4);
        colorsInPalette = readNextBytesInt(4);
        impColors = readNextBytesInt(4);
    }

    public int readNextBytesInt (int x) // x = number of bytes to read
    // begins at current index, reads up to index + 3 in little endian
    {
        //String a = "";
        int a =0; //something is wrong with this loop. 
        for (int i = 0; i<x; i++)
        {
            try{    
                a += f.readByte();
            }catch (IOException e){System.out.println("Sig");System.exit(0);}
        }
        //return Integer.reverseBytes(Integer.parseInt(a,2));
        return a;
    }

    public int readNextBytesIntUnsigned (int x) // x = number of bytes to read
    // begins at current index, reads up to index + 3 in little endian
    {
        int a =0; //something is wrong with this loop.
        for (int i = 0; i<x; i++)
        {
            try{    
                a += f.readUnsignedByte();
            }catch (IOException e){
                System.out.println("Uns");
                e.printStackTrace();
                System.exit(0);
            }
        }
        return a;
    }

    public void loadBMP(String fileName)  // loads a BMP file into memory 
    {
        FileInputStream in; 
        try{
            in = new FileInputStream(fileName);
        }catch(IOException e)
        {
            in = null;
            System.out.println("Load");
            System.exit(0);
        }
        f = new DataInputStream(in);
        loadVars();
        a = new DrawingTool(new SketchPad(height*2, width*2,0));
        readInBMP();
    }

    public void drawBMP(int x, int y)     // displays the BMP centered at (x, y)
    {
        a.setWidth(1); //back to 1
        a.up();
        a.move(-width/2 + x, -height/2 + y);
        a.setDirection(0);
        padding = width%4;
        int r,g,b;
        int index = 0;
        for (int h = 0; h<height; h++)
        {
            for (int w = 0; w<width; w++)
            {
                b = (int)pixelsOfImage.get(index).get(0);
                g = (int)pixelsOfImage.get(index).get(1);
                r = (int)pixelsOfImage.get(index).get(2);
                index++;
                a.setColor(new Color(r,g,b));
                a.down();
                a.forward(1);//back to 1
            }
            a.setColor(Color.white);
            a.forward(1);
            index++;
            a.setDirection(90);
            a.up();
            a.forward(1); // back to 1
            a.setDirection(0);
            a.backward(width+1);//back to 1
        }
    }

    public void readInBMP()
    {
        padding = width%4;
        int r,g,b;
        int index = 0;
        for (int h = 0; h<height; h++)
        {
            for (int w = 0; w<width; w++)
            {
                b = readNextBytesIntUnsigned(1);
                g = readNextBytesIntUnsigned(1);
                r = readNextBytesIntUnsigned(1);
                pixelsOfImage.add(new ArrayList<Integer>(3));
                pixelsOfImage.get(index).add(b);
                pixelsOfImage.get(index).add(g);
                pixelsOfImage.get(index).add(r);
                index++;

            }
            pixelsOfImage.add(new ArrayList<Integer>(padding));
            for (int i = 0; i<padding; i++)
            {
                pixelsOfImage.get(index).add(readNextBytesInt(1));
            }
            index++;
        }

    }

    public void drawBMP()
    {
        a.setWidth(1); //back to 1
        a.up();
        a.move(-width/2, -height/2);
        a.setDirection(0);
        padding = width%4;
        int r,g,b;
        int index = 0;
        for (int h = 0; h<height; h++)
        {
            for (int w = 0; w<width; w++)
            {
                b = (int)pixelsOfImage.get(index).get(0);
                g = (int)pixelsOfImage.get(index).get(1);
                r = (int)pixelsOfImage.get(index).get(2);
                index++;
                a.setColor(new Color(r,g,b));
                a.down();
                a.forward(1);//back to 1
            }
            a.setColor(Color.white);
            a.forward(1);
            index++;
            a.setDirection(90);
            a.up();
            a.forward(1); // back to 1
            a.setDirection(0);
            a.backward(width+1);//back to 1
        }

    }

    public void printBMPHeader()          // displays the BMP header info in console
    {
        System.out.println("ID Field: " + IDfield);
        System.out.println("Size of BMP File: " + size);
        System.out.println("Offset of Pixel Array: " + offsetToPixData);
        System.out.println("Size of header: " + bytesinDib);
        System.out.println("Bitmap width: " + width);
        System.out.println("Bitmap height: " + height);
        System.out.println("Number of color planes: " + planes);
        System.out.println("Number of bits per pixel: " + bitsPerPx);
        System.out.println("Compression method being used: " + BI_RGB);
        System.out.println("Size of data in pixel array: " + dataInPxArr);
        System.out.println("Horizontal resolution: " + horizRes);
        System.out.println("Vertical resolution: " + vertRes);
        System.out.println("Number of colors in palette: " + colorsInPalette);
        System.out.println("Important colors used: " + impColors);
    }

    //SECOND PART

    public String readFile (String fileName)
    {
        Scanner a; 
        try{
            a = new Scanner(new File (fileName));
        }catch (FileNotFoundException e)
        {
            a = null;
            System.out.println("READFILE");
            System.exit(0);
        }
        String f = "";
        while(a.hasNext())
        {
            f += a.nextLine() + "\r\n";
        }
        String ans = "";
        for (int i = 0; i<f.length(); i++)
        {
            ans += addLeadingZeroes(Integer.toBinaryString((int)f.charAt(i)));
        }
        return ans + "00000111";
    }

    public String addLeadingZeroes(String bin)
    {
        if (bin.length() ==8)
        {
            return bin;
        }else
        {
            while (bin.length() != 8)
            {
                bin = "0" + bin;
            }
            return bin;
        }
    }

    // Loads a text file and inserts it into the current BMP object
    // Precondition: loadBMP(bmpFileName) has already been called
    public void encodeBMP(String txtFileName)
    {
        String file = readFile(txtFileName); // already in binary
        int index = 0;
        outer:for(int i = 0; i<pixelsOfImage.size(); i++)
        {
            for (int j = 0; j<3; j++)
            {
                if ((pixelsOfImage.get(i).size() < 3))
                {
                    index++;
                    break;
                }
                if (index>=file.length())
                {
                    break outer;
                }
                int a = (int)pixelsOfImage.get(i).get(j);
                if (file.charAt(index) == '0' && (a % 2 )== 1)
                {
                    if ((a+1)<= 255){pixelsOfImage.get(i).set(j, a+1);}else {pixelsOfImage.get(i).set(j, a-1);}
                }
                boolean mod = (a % 2) == 0; //DEB
                char dd = file.charAt(index); //DEB
                boolean see = file.charAt(index) == '1'; //DEB
                
                if (file.charAt(index) == '1' && (a % 2) == 0)
                {
                    if ((a+1)<= 255){pixelsOfImage.get(i).set(j, a+1);}else {pixelsOfImage.get(i).set(j, a-1);}
                }
                index++;
            }
        }
    }

    // Extracts a message hidden in a BMP file and saves it into txtFileName
    // Precondition: loadBMP(bmpFileName) has already been called
    public void extractMessage(String txtFileName)
    {
        File q = new File (txtFileName);
        FileWriter writer; 
        //ArrayList<Character> c = new ArrayList<Character>();
        try{
            writer = new FileWriter(q);
        }catch (IOException e)
        {
            writer=null;
            System.out.println("FILEWRITER");
            System.exit(0);
        }
        String s = "";
        outer:for(int i = 0; i<pixelsOfImage.size(); i++)
        {
            for (int j = 0; j<3; j++)
            {
                if ((pixelsOfImage.get(i).size() < 3))
                {
                    break;
                }
                s += (int)pixelsOfImage.get(i).get(j) % 2 + "";
                if (s.length() == 8)
                {
                    if (Integer.parseInt(s,2) == 7)
                    {
                        break outer;
                    }else{
                        try{
                            writer.write(Integer.parseInt(s,2));
                            s = new String();
                        }catch (IOException e)
                        {
                            System.out.println("FWRITINGR");
                            System.exit(0);
                        }
                    }
                }
            }
        }
        
        try
        {
            writer.flush();
            writer.close();
        }catch (IOException e)
        {
            System.out.println("Writer.flush");
            System.exit(0);
        }
    }

    // Saves a BMP image to disk in Windows V3 format
    public void saveBMP(String bmpFileName)
    {
        DataOutputStream outStream; 
        try
        {
            outStream = new DataOutputStream(new FileOutputStream (bmpFileName)) ;
        }catch (FileNotFoundException e)
        {
            outStream = null; 
            System.out.println("saveBMP"); 
            System.exit(0);
        }

        try
        {
            outStream.writeBytes(IDfield);
            outStream.writeInt(Integer.reverseBytes(size));
            outStream.writeShort(Short.reverseBytes(appSpec1));
            outStream.writeShort(Short.reverseBytes(appSpec2));
            outStream.writeInt(Integer.reverseBytes(offsetToPixData));
            outStream.writeInt(Integer.reverseBytes(bytesinDib));
            outStream.writeInt(Integer.reverseBytes(width));
            outStream.writeInt(Integer.reverseBytes(height));
            outStream.writeShort(Short.reverseBytes(planes));
            outStream.writeShort(Short.reverseBytes(bitsPerPx));
            outStream.writeInt(Integer.reverseBytes(BI_RGB));
            outStream.writeInt(Integer.reverseBytes(dataInPxArr));
            outStream.writeInt(Integer.reverseBytes(horizRes));
            outStream.writeInt(Integer.reverseBytes(vertRes));
            outStream.writeInt(Integer.reverseBytes(colorsInPalette));
            outStream.writeInt(Integer.reverseBytes(impColors));
        }catch (IOException e)
        {
            System.out.println("WRITING");
            System.exit(0);
        }

        for (int i = 0; i<pixelsOfImage.size(); i++)
        {
            for (int j = 0; j<pixelsOfImage.get(i).size(); j++)
            {
                try{
                    outStream.writeInt(Integer.reverseBytes((int)pixelsOfImage.get(i).get(j)));
                }catch (IOException e){
                    System.out.println("writearray");
                    System.exit(0);
                }
            }
        }
        
        
        try{outStream.close();}catch (IOException e){System.exit(0);}
    }

    public static void main (String[] args)
    {
        BitmapUtilPrachiBodasPeriod3 jj = new BitmapUtilPrachiBodasPeriod3();
        jj.loadBMP("bitmap_size_test_2.bmp");
        jj.drawBMP();
        jj.encodeBMP("BitmapUtilPBodas.txt");
        jj.saveBMP("encodedbmp.bmp");
        //jj.loadBMP("encodedbmp.bmp");
        //jj.drawBMP();
        jj.extractMessage("extract.txt");
    }
}


