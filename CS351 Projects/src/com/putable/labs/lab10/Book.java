package com.putable.labs.lab10;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Book implements Iterable<Paragraph>
{
    private List<Paragraph> paragraphs = new ArrayList<Paragraph>();
    private Scanner bookReader;
    private boolean finishedRead = false;
    private boolean appendNewLine = false;
    
    public Book(String bookFile) throws IOException
    {
	File targetFile = new File(bookFile);
	bookReader = new Scanner(new FileReader(targetFile));
    }
    
    private boolean readNextParagraph()
    {
	return !finishedRead && readHelper();
    }
    
    private boolean readHelper()
    {
	StringBuilder paragraphBuilder = new StringBuilder();
	boolean endOfParagraph = false;
	if(appendNewLine)
	    paragraphBuilder.append("\n");
	while(!endOfParagraph && bookReader.hasNext())
	{
		String check = bookReader.nextLine();
		paragraphBuilder.append((endOfParagraph = check.isEmpty()) ? check : check);
	}
	String paragraph = paragraphBuilder.toString();
	appendNewLine = true;
	if(!paragraph.isEmpty())
	    paragraphs.add(new Paragraph(paragraph));
	return !paragraph.isEmpty();
    }

    @Override
    public Iterator<Paragraph> iterator()
    {
	if (paragraphs.size() == 0)
	{
	    final Book iteratingBook = this;
	    return new Iterator<Paragraph>()
	    {
		int currentIndex = 0;
		
		@Override
		public boolean hasNext()
		{
		    return currentIndex < iteratingBook.paragraphs.size() || iteratingBook.readNextParagraph();
		}

		@Override
		public Paragraph next()
		{
		    while(currentIndex >= iteratingBook.paragraphs.size() && iteratingBook.readNextParagraph());
		    return iteratingBook.paragraphs.get(currentIndex++);
		}

		@Override
		public void remove()
		{
		    throw new UnsupportedOperationException("You suck");
		}
	    };
	}
	else
	    return paragraphs.iterator();
    }
    
    public static void main(String[] args)
    {
	try
	{
	    Book readBook = new Book(args[0]);
	    for(Paragraph p : readBook)
		System.out.println(p);
	}
	catch(IOException e)
	{
	    System.err.println("Error with file!");
	    e.printStackTrace();
	}
    }
}
