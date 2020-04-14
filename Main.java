package dictionary;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Provided a grid of characters, find all valid words connected by any direction.
 *
 * Prerequisite: JDK 1.8
 *
 * Compile and run:
 * $javac Main.java
 * $java Main.java a,m,r b,e,a, o,u,t
 *
 * grid representation {'a', 'm', 'r'},
 *                     {'b', 'e', 'a'},
 *                     {'o', 'u', 't'}
 *
 * A few points I missed
 * 1. Was not removing current character after each DFS call
 * 2. Could add duplicate words to the result ArrayList. Use Set instead.
 * 3. Could have used StringBuilder directly for current sequence instead of iterating through ArrayList
 * 4. Could have assumed a Dictionary.isPrefix() method to decide continuing DFS or not
 */
public class Main {
    static Dictionary dictionary = new Dictionary();
    static String[] words = {"a", "ab", "about", "absolute", "arm", "be", "beat", "bear", "beam", "blue", "but",
            "mat", "met", "meat", "out", "rat", "rear", "so", "sol", "tar", "tea", "teal", "tear", "team", "term"};

    public static void main(String[] argc) {
        for (String word : words) {
            dictionary.addWord(word);
        }

        char[][] grid =
        {
                {'a', 'e', 't'},
                {'b', 'l', 'u'},
                {'s', 'o', 'l'}
        };

        // Take grid from argument
        if (argc.length > 1) {
            grid = new char[argc.length][argc.length]; // Assuming a square grid
            for (int i = 0; i < argc.length; i++) {
                String[] cells = argc[i].split(",");
                for (int j = 0; j < argc.length; j++) {
                    grid[i][j] = cells[j].toCharArray()[0]; // Assuming one character
                }
            }
        }



        // Should have been a Set instead of ArrayList to not have duplicate elements
        // Unless we want duplicate words derived from different path?
        TreeSet<String> words = new TreeSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                findWords(grid, i, j, new StringBuilder(), words);
            }
        }

        for (String word : words) {
            System.out.println(word);
        }
    }


    /**
     * DFS to look for valid words in a two dimensional character grid.
     */
    public static void findWords(char[][] grid, int i, int j, StringBuilder seq, TreeSet<String> words) {
        if (!(i > -1 && i < grid.length && j > -1 && j < grid[i].length)) return;

        // Don't need this check when only proceeding DFS if dictionary.isPrefix(string)
        // if (seq.size() > dictionary.getLongestWord().length) return;

        char c = grid[i][j];
//        Was using ArrayList to store the current sequence, could have used StringBuilder directly
//        seq.add(c);
//        StringBuilder builder = new StringBuilder();
//        for (Character character : seq) {
//            builder.append(character);
//        }
//        String string = builder.toString();

        seq.append(c);
        String string = seq.toString();
        if (dictionary.isWord(string)) {
            words.add(string);
        }

        // Should have had this check so don't even continue DFS if we know current sequence is not a prefix of any words
        if (dictionary.isPrefix(string)) {
            findWords(grid, i+1, j, seq, words);
            findWords(grid, i-1, j, seq, words);
            findWords(grid, i, j+1, seq, words);
            findWords(grid, i, j-1, seq, words);
            findWords(grid, i+1, j+1, seq, words);
            findWords(grid, i+1, j-1, seq, words);
            findWords(grid, i-1, j+1, seq, words);
            findWords(grid, i-1, j-1, seq, words);
        }

        // Was missing this to remove current char before getting out of this path
        seq.deleteCharAt(seq.length()-1);
        // seq.remove(seq.size()-1); // ArrayList
    }

    /**
     * Trie implementation
     */
    static class Dictionary {
        Node root = new Node('0');

        public void addWord(String word) {
            Node node = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                node.children.putIfAbsent(c, new Node(c));
                node = node.getChild(c);
            }
            node.end = true;
        }

        public boolean isWord(String string) {
            Node node = root;
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                node = node.getChild(c);
                if (node == null) return false;
            }
            return node.end;
        }

        public boolean isPrefix(String string) {
            Node node = root;
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                node = node.getChild(c);
                if (node == null) return false;
            }
            return true;
        }

        public int getLongestWordLength() {
            // TODO: DFS to find the length of the tree
            return -1;
        }

    }

    static class Node {
        char c;
        boolean end;
        HashMap<Character, Node> children;

        public Node(char c) {
            this.c = c;
            children = new HashMap<>();
        }

        public Node getChild(char c) {
            return children.get(c);
        }
    }

}
