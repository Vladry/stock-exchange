package exchange;

import java.io.*;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Main {


    private static int returnSize(int price, Queue<? extends AbstractEntity> queue) {
        int size = 0;
        for (AbstractEntity obj : queue) {
            if (obj.getPrice() == price) {
                size += obj.getSize();
            }
        }
        return size;
    }

    private static int processShares(int orderSize, Queue<? extends AbstractEntity> o) {
        if (orderSize == 0) {
            return 0;
        }
        if (o.peek() == null) {
            return 0;
        }

        int sharesAvailable = o.peek().getSize();
        if (orderSize >= sharesAvailable) {
            o.poll();
            processShares(orderSize - sharesAvailable, o);
        } else {
            o.peek().setSize(sharesAvailable - orderSize);
        }
        return 0;
    }


    private static void writeToFile(String outputString) {
        final String OUT_FILE_NAME = "output.txt";
        File outputFile = new File(OUT_FILE_NAME);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                System.out.println("error creating file " + OUT_FILE_NAME);
                System.out.println(e.getMessage());
            }
        }

        try (FileWriter fw = new FileWriter(outputFile, true)) {
            fw.write(outputString+"\n");
        } catch (IOException e) {
            System.out.println("error writing to file " + OUT_FILE_NAME);
            System.out.println(e.getMessage());
        }

    }


    public static void main(String[] args) throws IOException {
        final String IN_FILE_NAME = "input.txt";
        final String DELIMITER = ",";
        String outputString;

        File inputFile = new File(IN_FILE_NAME);
        if (!inputFile.exists()) {
            System.out.println("file " + IN_FILE_NAME + " not found in working directory!");
            System.out.println("getName(): " + inputFile.getName());
            System.out.println("getAbsolutePath(): " + inputFile.getAbsolutePath());
            System.out.println("getParent(): " + inputFile.getParent());
            return;
        }


        final PriorityQueue<Bid> bids = new PriorityQueue<>();
        final PriorityQueue<Ask> asks = new PriorityQueue<>();

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            while ((line = br.readLine()) != null) {
                String[] str = line.split(DELIMITER);
                String selector = str[0];

                switch (selector) {


                    case "u", "U":
                        int price = Integer.parseInt(str[1]);
                        int updateSize = Integer.parseInt(str[2]);
                        if (str[3].equalsIgnoreCase("bid")) {
                            bids.offer(new Bid(price, updateSize));
                        } else {
                            asks.offer(new Ask(price, updateSize));
                        }
                        break;

                    case "o", "O":
                        String action = str[1];
                        int orderSize = Integer.parseInt(str[2]);
                        if (action.equalsIgnoreCase("buy")) {
                            processShares(orderSize, asks);

                        } else if (action.equalsIgnoreCase("sell")) {
                            processShares(orderSize, bids);
                        }
                        break;


                    case "q", "Q":
                        if (str.length < 3) {
                            String task = str[1];
                            switch (task) {
                                case "best_bid":
//                                    System.out.println("best bid: " + bids.peek());
                                    outputString = bids.peek().getPrice() + "," + bids.peek().getSize();
                                    writeToFile(outputString);
                                    break;
                                case "best_ask":
//                                    System.out.println("best ask: " + asks.peek());
                                    outputString = asks.peek().getPrice() + "," + asks.peek().getSize();
                                    writeToFile(outputString);
                                    break;

                                default:
                            }
                        } else if (str[1].equalsIgnoreCase("size")) {
                            int specifiedPrice = Integer.parseInt(str[2]);
                            int size = 0;
                            if (specifiedPrice <= bids.peek().getPrice()) {
                                size = returnSize(specifiedPrice, bids);
                            } else if (specifiedPrice >= asks.peek().getPrice()) {
                                size = returnSize(specifiedPrice, asks);
                            }

//                            System.out.println("shares at price "+specifiedPrice+ ": " + size);
                            outputString = String.valueOf(size);
                            writeToFile(outputString);
                        }

                        break;


                    default:

                }

            }
        } catch (IOException e) {
        }

    }

}
