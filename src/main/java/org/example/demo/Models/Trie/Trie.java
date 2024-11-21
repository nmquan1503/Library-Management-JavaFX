package org.example.demo.Models.Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public Trie(TrieNode root) {
        this.root = root;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void insertNode(String name, int id) {
        TrieNode root1 = new TrieNode();
        root1 = root;
        for (char x : name.toCharArray()) {
            Character x1 = x;
            x1=Character.toLowerCase(x1);
            HashMap<Character, TrieNode> children = root1.getChildren();
            TrieNode getNode = children.get(x1);
            if (getNode == null) {
                root1.addChildren(x1, new TrieNode());
                root1 = root1.getChildren().get(x1);
            } else {
                root1 = getNode;
            }
        }
        if (!root1.getListID().contains(id)) {
            root1.addList(id);
        }
    }

    public void deleteNode(String name, int id) {
        TrieNode root1 = new TrieNode();
        root1 = root;
        int i = 0;
        for (char x : name.toCharArray()) {
            Character x1 = x;
            HashMap<Character, TrieNode> children = root1.getChildren();
            TrieNode getNode = children.get(x1);
            if (getNode == null) {
                break;
            } else {
                root1 = getNode;
                i++;
            }
        }
        if (i == name.length()) {
            root1.deleteList(id);
        }
    }

    public void dfs(TrieNode root1, ArrayList<Integer> res) {
        HashMap<Character, TrieNode> map = root1.getChildren();
        if (root1.getListID().size() > 0) {
            for (Integer x : root1.getListID()) {
                res.add(x);
            }
        }
        for (Map.Entry<Character, TrieNode> entry : map.entrySet()) {
            dfs(entry.getValue(), res);
        }
    }

    public ArrayList<Integer> getListIdStartWith(String prefix) {
        TrieNode root1 = root;
        ArrayList<Integer> res = new ArrayList<>();

        int i = 0;
        while (i < prefix.length()) {
            boolean check = false;
            HashMap<Character, TrieNode> map = root1.getChildren();
            TrieNode nextNode = map.get(prefix.charAt(i));
            if ( nextNode == null ) {
                char x = prefix.charAt(i);
                if ( Character.isUpperCase(x) ) x = Character.toLowerCase(x);
                else x = Character.toUpperCase(x);
                nextNode = map.get(x);
                if ( nextNode == null ) break;
            }
            root1=nextNode;
            i++;
        }
        if (i == prefix.length()) {
            dfs(root1, res);
        }
        return res;
    }

    public void printNode(TrieNode root, String s) {
        TrieNode root1 = root;
        HashMap<Character, TrieNode> map = root1.getChildren();
        if (root1.getListID().size() > 0) {
            System.out.println(s);
        }
        for (Map.Entry<Character, TrieNode> entry : map.entrySet()) {
            printNode(entry.getValue(), s + (char) entry.getKey());
        }
    }
    
    public ArrayList<String> getAllNameStartWith(String prefix){
        ArrayList<String> list=new ArrayList<>();
        TrieNode tmpRoot=root;
        for(int i=0;i<prefix.length();i++){
            if(tmpRoot==null)return list;
            tmpRoot=tmpRoot.getChildren().get(prefix.charAt(i));
        }
        if(tmpRoot==null)return list;
        addNameToList(tmpRoot,list,prefix);
        return list;
    }
    private void addNameToList(TrieNode trieNode,ArrayList<String> list,String name){
        if(!trieNode.getListID().isEmpty()){
            list.add(name);
        }
        for(Map.Entry<Character,TrieNode> entry:trieNode.getChildren().entrySet()){
            addNameToList(entry.getValue(),list,name+entry.getKey());
        }
    }

}
