package org.example.demo.Models.Trie;

import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {
    private ArrayList<Integer>listID;
    private HashMap<Character,TrieNode> children;
    public TrieNode() {

    }
    public TrieNode(ArrayList<Integer> listID) {
        this.listID=listID;
    }

    public ArrayList<Integer> getListID() {
        return listID;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }
}
