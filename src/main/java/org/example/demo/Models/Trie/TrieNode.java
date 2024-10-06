package org.example.demo.Models.Trie;

import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {

    private ArrayList<Integer> listID;
    private HashMap<Character, TrieNode> children;

    public TrieNode() {
        children = new HashMap<>();
        listID = new ArrayList<>();
    }

    public TrieNode(ArrayList<Integer> listID) {
        this.listID = listID;
        children = new HashMap<>();
    }

    public TrieNode(ArrayList<Integer> listID, HashMap<Character, TrieNode> children) {
        this.listID = listID;
        this.children = children;
    }

    public ArrayList<Integer> getListID() {
        return listID;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public void setListID(ArrayList<Integer> listID) {
        this.listID = listID;
    }

    public void setChildren(
            HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public void addChildren(Character x, TrieNode y) {
        children.put(x, y);
    }

    public void addList(Integer id) {
        listID.add(id);
    }

    public void deleteList(Integer id) {
        listID.remove(Integer.valueOf(id));
    }
}
