package com.giangvu.notepad;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ConnectDOM {
    private Document document;
    private String path = null;

    public ConnectDOM(String file) {
        this.path = file;
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        if (FileExists(file)) {
            try {
                documentBuilder = fac.newDocumentBuilder();
                FileInputStream input = new FileInputStream(file);
                this.document = documentBuilder.parse(input);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                CreateFileXML(file);
                documentBuilder = fac.newDocumentBuilder();
                this.document = documentBuilder.newDocument();
                Element root = document.createElement("notes");
                document.appendChild(root);
                RewriteFileByDOM(document);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean FileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void CreateFileXML(String path) {
        File newNoteXML = new File(path);
        try {
            newNoteXML.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NoteItem> ReadByDOM() {
        ArrayList<NoteItem> items = new ArrayList<NoteItem>();
        NoteItem item = null;
        String title = "", content = "",id="";
        if (document == null) {
            return null;
        }
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                id = element.getAttribute("id");
                title = element.getAttribute("title");
                NodeList nodeListChild = element.getElementsByTagName("content");
                content = nodeListChild.item(0).getTextContent();
                item = new NoteItem(Integer.parseInt(id), title,content);
                items.add(item);
            }
        }
        return items;
    }

    public void DeleteNoteByDOM(NoteItem note) {
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                if (id.equals(note.getId()+"")) {
                    node.getParentNode().removeChild(node);
                    RewriteFileByDOM(document);
                    return;
                }
            }
        }
    }

    public void CreateNewByDOM(NoteItem note) {
        Element root = document.getDocumentElement();

        Element newNote = document.createElement("note");
        newNote.setAttribute("id", note.getId()+"");
        newNote.setAttribute("title", note.getTitle());
        Element newContent = document.createElement("content");
        newContent.appendChild(document.createTextNode(note.getContent()));
        newNote.appendChild(newContent);

        root.appendChild(newNote);
        RewriteFileByDOM(document);
    }

    public void SaveByDOM(NoteItem note) {
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                if (id.equals(note.getId()+"")) {
                    NodeList nodeListChild = element.getElementsByTagName("content");
                    nodeListChild.item(0).setTextContent(note.getContent());
                    RewriteFileByDOM(document);
                    return;
                }
            }
        }
    }

    public void RewriteFileByDOM(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            FileOutputStream outStream = new FileOutputStream(new File(path));
            transformer.transform(new DOMSource(doc), new StreamResult(outStream));
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
