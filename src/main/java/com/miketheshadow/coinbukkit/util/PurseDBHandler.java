package com.miketheshadow.coinbukkit.util;

import com.miketheshadow.complexproficiencies.crafting.Category;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PurseDBHandler
{

    private static  MongoCollection<Document> collection = init();

    public static boolean addPurse(Purse purse) {
        FindIterable<Document> cursor = collection.find(new BasicDBObject("name", purse.getName()));
        if (cursor.first() == null) {
            collection.insertOne(purse.toDocument());
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Adding new purse: " + purse.getName());
            return true;
        }
        return false;
    }

    public static Purse getPurse(String name){
        FindIterable<Document> cursor = collection.find(new BasicDBObject("name", name));
        if(cursor.first() == null) return null;
        return new Purse(cursor.first());
    }

    public static void updatePurse(Category category) {
        collection.replaceOne(new BasicDBObject("path", category.getPath()), category.toDocument());
    }

    public static boolean removePurse(String purse){
        FindIterable<Document> cursor = collection.find(new BasicDBObject("name", purse));
        Document remove = cursor.first();
        if(remove == null) return false;
        collection.deleteOne(remove);
        return true;
    }

    public static List<Purse> getAllPurses() {
        List<Purse> purseList = new ArrayList<>();
        for (Document doc: collection.find())
        {
            purseList.add(new Purse(doc));
        }
        return purseList;
    }

    public static Purse getPurseByMinCost(int cost) {
        FindIterable<Document> cursor = collection.find( new Document()
                .append("minLevel", new Document()
                                .append("$gte" , cost)));
        return new Purse(cursor.first());
    }

    public static MongoCollection<Document> init() {
        if(collection == null) {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            MongoDatabase database = mongoClient.getDatabase("ComplexProficiencies");
            return database.getCollection("Purses");
        }
        return collection;
    }
    //for fixing documents
    public static List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        for (Document document: collection.find()) {
            documents.add(document);
        }
        return documents;
    }
    public static void updateDocument(Document document) {
        collection.replaceOne(new BasicDBObject("name",document.get("name")),document);
    }
}
