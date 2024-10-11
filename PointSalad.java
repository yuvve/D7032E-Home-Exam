import java.util.Collections;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PointSalad {
	public ArrayList<Player> players = new ArrayList<>();
	public ArrayList<Pile> piles = new ArrayList<>();
    public ServerSocket aSocket;

	class Player {
		public int playerID;
		public boolean online;
		public boolean isBot;
		public Socket connection;
		public ObjectInputStream inFromClient;
		public ObjectOutputStream outToClient;
		public ArrayList<String> region = new ArrayList<String>();
		Scanner in = new Scanner(System.in);
		public ArrayList<Card> hand = new ArrayList<Card>();
		public int score = 0;



		public Player(int playerID, boolean isBot, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) {
			this.playerID = playerID; this.connection = connection; this.inFromClient = inFromClient; this.outToClient = outToClient; this.isBot = isBot;
			if(connection == null)
				this.online = false;
			else
				this.online = true;
		}
		public void sendMessage(Object message) {
			if(online) {
				try {outToClient.writeObject(message);} catch (Exception e) {}
			} else if(!isBot){
				System.out.println(message);                
			}
		}
		public String readMessage() {
			String word = ""; 
			if(online)
				try{word = (String) inFromClient.readObject();} catch (Exception e){}
			else
				try {word=in.nextLine();} catch(Exception e){}
			return word;
		}
	}

	public class Pile {
		public ArrayList<Card> cards = new ArrayList<Card>();
		public Card[] veggieCards = new Card[2];

		public Pile(ArrayList<Card> cards) {
			this.cards = cards;
			this.veggieCards[0] = cards.remove(0);
			this.veggieCards[1] = cards.remove(0);	
			this.veggieCards[0].criteriaSideUp = false;;
			this.veggieCards[1].criteriaSideUp = false;
		}
		public Card getPointCard() {
			if(cards.isEmpty()) {
				//remove from the bottom of the biggest of the other piles
				int biggestPileIndex = 0;
				int biggestSize = 0;
				for(int i = 0; i < piles.size(); i++) {
					if(i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
						biggestSize = piles.get(i).cards.size();
						biggestPileIndex = i;
					}
				}
				if(biggestSize > 1) {
					cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size()-1));
				} else // we can't remove active point cards from other piles
					return null;
				}
			return cards.get(0);
		}
		public Card buyPointCard() {
			if(cards.isEmpty()) {
				//remove from the bottom of the biggest of the other piles
				int biggestPileIndex = 0;
				int biggestSize = 0;
				for(int i = 0; i < piles.size(); i++) {
					if(i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
						biggestSize = piles.get(i).cards.size();
						biggestPileIndex = i;
					}
				}
				if(biggestSize > 1) {
					cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size()-1));
				} else { // we can't remove active point cards from other piles
					return null;
				} 
			}
			return cards.remove(0);
		}
		public Card getVeggieCard(int index) {
			return veggieCards[index];
		}
		public Card buyVeggieCard(int index) {
			Card aCard = veggieCards[index];
			if(cards.size() <=1) {
				//remove from the bottom of the biggest of the other piles
				int biggestPileIndex = 0;
				int biggestSize = 0;
				for(int i = 0; i < piles.size(); i++) {
					if(i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
						biggestSize = piles.get(i).cards.size();
						biggestPileIndex = i;
					}
				}
				if(biggestSize > 1) {
					cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size()-1));
					veggieCards[index] = cards.remove(0);
					veggieCards[index].criteriaSideUp = false;
				} else {
					veggieCards[index] = null;
				}
			} else {
				veggieCards[index] = cards.remove(0);
				veggieCards[index].criteriaSideUp = false;
			}

			return aCard;
		}
		public boolean isEmpty() {
			return cards.isEmpty() && veggieCards[0] == null && veggieCards[1] == null;
		}
	}

	public static class Card {
		public enum Vegetable {
			PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
		}

		public Vegetable vegetable;
		public String criteria;
		public boolean criteriaSideUp = true;

		public Card(Vegetable vegetable, String criteria) {
			this.vegetable = vegetable;
			this.criteria = criteria;
		}

		@Override
		public String toString() {
			if(criteriaSideUp) {
				return criteria + " (" + vegetable + ")";
			} else {
				return vegetable.toString();
			}
		}
	}

    public void setPiles(int nrPlayers) {
        ArrayList<Card> deckPepper = new ArrayList<>();
        ArrayList<Card> deckLettuce = new ArrayList<>();
		ArrayList<Card> deckCarrot = new ArrayList<>();
		ArrayList<Card> deckCabbage = new ArrayList<>();
		ArrayList<Card> deckOnion = new ArrayList<>();
		ArrayList<Card> deckTomato = new ArrayList<>();

        try (InputStream fInputStream = new FileInputStream("PointSaladManifest.json");
             Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

            // Read the entire JSON file into a String
            String jsonString = scanner.hasNext() ? scanner.next() : "";

            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the "cards" array from the JSONObject
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Iterate over each card in the array
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardJson = cardsArray.getJSONObject(i);

                // Get the criteria object from the card JSON
                JSONObject criteriaObj = cardJson.getJSONObject("criteria");

                // Add each vegetable card to the deck with its corresponding criteria
                deckPepper.add(new Card(Card.Vegetable.PEPPER, criteriaObj.getString("PEPPER")));
                deckLettuce.add(new Card(Card.Vegetable.LETTUCE, criteriaObj.getString("LETTUCE")));
                deckCarrot.add(new Card(Card.Vegetable.CARROT, criteriaObj.getString("CARROT")));
                deckCabbage.add(new Card(Card.Vegetable.CABBAGE, criteriaObj.getString("CABBAGE")));
                deckOnion.add(new Card(Card.Vegetable.ONION, criteriaObj.getString("ONION")));
                deckTomato.add(new Card(Card.Vegetable.TOMATO, criteriaObj.getString("TOMATO")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffle each deck
		shuffleDeck(deckPepper);
		shuffleDeck(deckLettuce);
		shuffleDeck(deckCarrot);
		shuffleDeck(deckCabbage);
		shuffleDeck(deckOnion);
		shuffleDeck(deckTomato);

		int cardsPerVeggie = nrPlayers/2 * 6;
		
		ArrayList<Card> deck = new ArrayList<>();
		for(int i = 0; i < cardsPerVeggie; i++) {
			deck.add(deckPepper.remove(0));
			deck.add(deckLettuce.remove(0));
			deck.add(deckCarrot.remove(0));
			deck.add(deckCabbage.remove(0));
			deck.add(deckOnion.remove(0));
			deck.add(deckTomato.remove(0));
		}
		shuffleDeck(deck);

		//divide the deck into 3 piles
		ArrayList<Card> pile1 = new ArrayList<>();
		ArrayList<Card> pile2 = new ArrayList<>();
		ArrayList<Card> pile3 = new ArrayList<>();
		for (int i = 0; i < deck.size(); i++) {
			if (i % 3 == 0) {
				pile1.add(deck.get(i));
			} else if (i % 3 == 1) {
				pile2.add(deck.get(i));
			} else {
				pile3.add(deck.get(i));
			}
		}
		piles.add(new Pile(pile1));
		piles.add(new Pile(pile2));
		piles.add(new Pile(pile3));
    }

	public int calculateScore(ArrayList<Card> hand, Player thisPlayer) {
		//System.out.println("DEBUG: \n" + displayHand(hand));
		int totalScore = 0;

		for (Card criteriaCard : hand) {
			if (criteriaCard.criteriaSideUp) {
				String criteria = criteriaCard.criteria;
				String[] parts = criteria.split(",");

				//ID 18
				if(criteria.indexOf("TOTAL") >= 0 || criteria.indexOf("TYPE") >= 0 || criteria.indexOf("SET") >= 0) {
					if(criteria.indexOf("TOTAL")>=0) {
						int countVeg = countTotalVegetables(hand);
						int thisHandCount = countVeg;
						for(Player p : players) {
							if(p.playerID != thisPlayer.playerID) {
								int playerVeg = countTotalVegetables(p.hand);
								if((criteria.indexOf("MOST")>=0) && (playerVeg > countVeg)) {
									countVeg = countTotalVegetables(p.hand);
								}
								if((criteria.indexOf("FEWEST")>=0) && (playerVeg < countVeg)) {
									countVeg = countTotalVegetables(p.hand);
								}
							}
						}
						if(countVeg == thisHandCount) {
							//int aScore = Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
							//System.out.print("ID18 MOST/FEWEST: "+aScore + " " );
							totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
						}
					}
					if(criteria.indexOf("TYPE")>=0) {
						String[] expr = criteria.split("/");
						int addScore = Integer.parseInt(expr[0].trim());
						if(expr[1].indexOf("MISSING")>=0) {
							int missing = 0;
							for (Card.Vegetable vegetable : Card.Vegetable.values()) {
								if(countVegetables(hand, vegetable) == 0) {
									missing++;
								}
							}
							//int aScore = missing * addScore;
							//System.out.print("ID18 TYPE MISSING: "+aScore + " ");
							totalScore += missing * addScore;
						}
						else {
							int atLeastPerVegType = Integer.parseInt(expr[1].substring(expr[1].indexOf(">=")+2).trim());
							int totalType = 0;
							for(Card.Vegetable vegetable : Card.Vegetable.values()) {
								int countVeg = countVegetables(hand, vegetable);
								if(countVeg >= atLeastPerVegType) {
									totalType++;
								}
							}
							//int aScore = totalType * addScore;
							//System.out.print("ID18 TYPE >=: "+aScore + " ");
							totalScore += totalType * addScore;
						}
					}
					if(criteria.indexOf("SET")>=0) {
						int addScore = 12;
						for (Card.Vegetable vegetable : Card.Vegetable.values()) {
							int countVeg = countVegetables(hand, vegetable);
							if(countVeg == 0) {
								addScore = 0;
								break;
							}
						}
						//System.out.print("ID18 SET: "+addScore + " ");
						totalScore += addScore;
					}
				}
				//ID1 and ID2
				else if((criteria.indexOf("MOST")>=0) || (criteria.indexOf("FEWEST")>=0)) { //ID1, ID2
					int vegIndex = criteria.indexOf("MOST")>=0 ? criteria.indexOf("MOST")+5 : criteria.indexOf("FEWEST")+7;
					String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();
					int countVeg = countVegetables(hand, Card.Vegetable.valueOf(veg));
					int nrVeg = countVeg;
					for(Player p : players) {
						if(p.playerID != thisPlayer.playerID) {
							int playerVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
							if((criteria.indexOf("MOST")>=0) && (playerVeg > nrVeg)) {
								nrVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
							}
							if((criteria.indexOf("FEWEST")>=0) && (playerVeg < nrVeg)) {
								nrVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
							}
						}
					}
					if(nrVeg == countVeg) {
						//System.out.print("ID1/ID2: "+Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim()) + " ");
						totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
					}
				}
			
				//ID3, ID4, ID5, ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16, ID17
				else if(parts.length > 1 || criteria.indexOf("+")>=0 || parts[0].indexOf("/")>=0) {
					if(criteria.indexOf("+")>=0) { //ID5, ID6, ID7, ID11, ID12, ID13
						String expr = criteria.split("=")[0].trim();
						String[] vegs = expr.split("\\+");
						int[] nrVeg = new int[vegs.length];
						int countSameKind = 1;
						for(int j = 1; j < vegs.length; j++) {
							if(vegs[0].trim().equals(vegs[j].trim())) {
								countSameKind++;
							}
						}
						if(countSameKind > 1) {
							//System.out.print("ID5/ID11: "+ ((int)countVegetables(hand, Card.Vegetable.valueOf(vegs[0].trim()))/countSameKind) * Integer.parseInt(criteria.split("=")[1].trim()) + " ");
							totalScore +=  ((int)countVegetables(hand, Card.Vegetable.valueOf(vegs[0].trim()))/countSameKind) * Integer.parseInt(criteria.split("=")[1].trim());
						} else {
							for(int i = 0; i < vegs.length; i++) {
								nrVeg[i] = countVegetables(hand, Card.Vegetable.valueOf(vegs[i].trim()));
							}
							//find the lowest number in the nrVeg array
							int min = nrVeg[0];
							for (int x = 1; x < nrVeg.length; x++) {
								if (nrVeg[x] < min) {
									min = nrVeg[x];
								}
							}
							//System.out.print("ID6/ID7/ID12/ID13: "+min * Integer.parseInt(criteria.split("=")[1].trim()) + " ");
							totalScore += min * Integer.parseInt(criteria.split("=")[1].trim());
						}
					}
					else if(parts[0].indexOf("=")>=0) { //ID3
						String veg = parts[0].substring(0, parts[0].indexOf(":"));
						int countVeg = countVegetables(hand, Card.Vegetable.valueOf(veg));
						//System.out.print("ID3: "+((countVeg%2==0)?7:3) + " ");
						totalScore += (countVeg%2==0)?7:3;
					}
					else { //ID4, ID8, ID9, ID10, ID14, ID15, ID16, ID17
						for(int i = 0; i < parts.length; i++) {
							String[] veg = parts[i].split("/");
							//System.out.print("ID4/ID8/ID9/ID10/ID14/ID15/ID16/ID17: " + Integer.parseInt(veg[0].trim()) * countVegetables(hand, Card.Vegetable.valueOf(veg[1].trim())) + " ");
							totalScore += Integer.parseInt(veg[0].trim()) * countVegetables(hand, Card.Vegetable.valueOf(veg[1].trim()));
						}
					}
				}
			}
		}
		return totalScore;
	}

	private int countVegetables(ArrayList<Card> hand, Card.Vegetable vegetable) {
		int count = 0;
		for (Card card : hand) {
			if (!card.criteriaSideUp && card.vegetable == vegetable) {
				count++;
			}
		}
		return count;
	}

	private int countTotalVegetables(ArrayList<Card> hand) {
		int count = 0;
		for (Card card : hand) {
			if (!card.criteriaSideUp) {
				count++;
			}
		}
		return count;
	}

	public void shuffleDeck(ArrayList<Card> deck) {
		Collections.shuffle(deck);
	}

	public String displayHand(ArrayList<Card> hand) {
		String handString = "Criteria:\t";
		for (int i = 0; i < hand.size(); i++) {
			if(hand.get(i).criteriaSideUp && hand.get(i).vegetable != null) {
				handString += "["+i+"] "+hand.get(i).criteria + " ("+hand.get(i).vegetable.toString()+")"+"\t";
			}
		}
		handString += "\nVegetables:\t";
		//Sum up the number of each vegetable and show the total number of each vegetable
		for (Card.Vegetable vegetable : Card.Vegetable.values()) {
			int count = countVegetables(hand, vegetable);
			if(count > 0) {
				handString += vegetable + ": " + count + "\t";
			}
		}
		return handString;
	}

	private void sendToAllPlayers(String message) {
		for(Player player : players) {
			player.sendMessage(message);
		}
	}

	public void client(String ipAddress) throws Exception {
        //Connect to server
        Socket aSocket = new Socket(ipAddress, 2048);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
        String nextMessage = "";
        while(!nextMessage.contains("winner")){
            nextMessage = (String) inFromServer.readObject();
            System.out.println(nextMessage);
            if(nextMessage.contains("Take") || nextMessage.contains("into")) {
                Scanner in = new Scanner(System.in);
                outToServer.writeObject(in.nextLine());
            } 
        }
    }

    public void server(int numberPlayers, int numberOfBots) throws Exception {
        players.add(new Player(0, false, null, null, null)); //add this instance as a player
        //Open for connections if there are online players
        for(int i=0; i<numberOfBots; i++) {
            players.add(new Player(i+1, true, null, null, null)); //add a bot    
        }
        if(numberPlayers>1)
            aSocket = new ServerSocket(2048);
        for(int i=numberOfBots+1; i<numberPlayers+numberOfBots; i++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new Player(i, false, connectionSocket, inFromClient, outToClient)); //add an online client
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }    
    }

	private String printMarket() {
		String pileString = "Point Cards:\t";
		for (int p=0; p<piles.size(); p++) {
			if(piles.get(p).getPointCard()==null) {
				pileString += "["+p+"]"+String.format("%-43s", "Empty") + "\t";
			}
			else
				pileString += "["+p+"]"+String.format("%-43s", piles.get(p).getPointCard()) + "\t";
		}
		pileString += "\nVeggie Cards:\t";
		char veggieCardIndex = 'A';
		for (Pile pile : piles) {
			pileString += "["+veggieCardIndex+"]"+String.format("%-43s", pile.getVeggieCard(0)) + "\t";
			veggieCardIndex++;
		}
		pileString += "\n\t\t";
		for (Pile pile : piles) {
			pileString += "["+veggieCardIndex+"]"+String.format("%-43s", pile.getVeggieCard(1)) + "\t";
			veggieCardIndex++;
		}
		return pileString;
	}


	public PointSalad(String[] args) {
		int numberPlayers = 0;
		int numberOfBots = 0;
		if(args.length == 0) {
			System.out.println("Please enter the number of players (1-6): ");
			Scanner in = new Scanner(System.in);
			numberPlayers = in.nextInt();
			System.out.println("Please enter the number of bots (0-5): ");
			numberOfBots = in.nextInt();
		}
		else {
			//check if args[0] is a String (ip address) or an integer (number of players)
			if(args[0].matches("\\d+")) {
				numberPlayers = Integer.parseInt(args[0]);
				numberOfBots = Integer.parseInt(args[1]);
			}
			else {
				try {
					client(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		setPiles(numberPlayers+numberOfBots);

		try {
			server(numberPlayers, numberOfBots);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set random starting player
		int currentPlayer = (int) (Math.random() * (players.size()));
		boolean keepPlaying = true;

		while(keepPlaying) {
			Player thisPlayer = players.get(currentPlayer);
			boolean stillAvailableCards = false;
			for(Pile p: piles) {
				if(!p.isEmpty()) {
					stillAvailableCards = true;
					break;
				}
			}
			if(!stillAvailableCards) {
				keepPlaying = false;
				break;
			}
			if(!thisPlayer.isBot) {
				thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
				thisPlayer.sendMessage(displayHand(thisPlayer.hand));
				thisPlayer.sendMessage("\nThe piles are: ");
			
				thisPlayer.sendMessage(printMarket());
				boolean validChoice = false;
				while(!validChoice) {
					thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
					String pileChoice = thisPlayer.readMessage();
					if(pileChoice.matches("\\d")) {
						int pileIndex = Integer.parseInt(pileChoice);
						if(piles.get(pileIndex).getPointCard() == null) {
							thisPlayer.sendMessage("\nThis pile is empty. Please choose another pile.\n");
							continue;
						} else {
							thisPlayer.hand.add(piles.get(pileIndex).buyPointCard());
							thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
							validChoice = true;
						}
					} else {
						int takenVeggies = 0;
						for(int charIndex = 0; charIndex < pileChoice.length(); charIndex++) {
							if(Character.toUpperCase(pileChoice.charAt(charIndex)) < 'A' || Character.toUpperCase(pileChoice.charAt(charIndex)) > 'F') {
								thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
								validChoice = false;
								break;
							}
							int choice = Character.toUpperCase(pileChoice.charAt(charIndex)) - 'A';
							int pileIndex = (choice == 0 || choice == 3) ? 0 : (choice == 1 || choice == 4) ? 1 : (choice == 2 || choice == 5) ? 2:-1;
							int veggieIndex = (choice == 0 || choice == 1 || choice == 2) ? 0 : (choice == 3 || choice == 4 || choice == 5) ? 1 : -1;
							if(piles.get(pileIndex).veggieCards[veggieIndex] == null) {
								thisPlayer.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
								validChoice = false;
								break;
							} else {
								if(takenVeggies == 2) {
									validChoice = true;
									break;
								} else {
									thisPlayer.hand.add(piles.get(pileIndex).buyVeggieCard(veggieIndex));
									takenVeggies++;
									//thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
									validChoice = true;
								}
							}
						}

					}
				}
				//Check if the player has any criteria cards in their hand
				boolean criteriaCardInHand = false;
				for(Card card : thisPlayer.hand) {
					if(card.criteriaSideUp) {
						criteriaCardInHand = true;
						break;
					}
				}
				if(criteriaCardInHand) {
					//Give the player an option to turn a criteria card into a veggie card
					thisPlayer.sendMessage("\n"+displayHand(thisPlayer.hand)+"\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
					String choice = thisPlayer.readMessage();
					if(choice.matches("\\d")) {
						int cardIndex = Integer.parseInt(choice);
						thisPlayer.hand.get(cardIndex).criteriaSideUp = false;
					}
				}
				thisPlayer.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
				sendToAllPlayers("Player " + thisPlayer.playerID + "'s hand is now: \n"+displayHand(thisPlayer.hand)+"\n");	

			} else {
				// Bot logic
				// The Bot will randomly decide to take either one point card or two veggie cards 
				// For point card the Bot will always take the point card with the highest score
				// If there are two point cards with the same score, the bot will take the first one
				// For Veggie cards the Bot will pick the first one or two available veggies
				boolean emptyPiles = false;
				// Random choice: 
				int choice = (int) (Math.random() * 2);
				if(choice == 0) {
					// Take a point card
					int highestPointCardIndex = 0;
					int highestPointCardScore = 0;
					for(int i = 0; i < piles.size(); i++) {
						if(piles.get(i).getPointCard() != null) {
							ArrayList<Card> tempHand = new ArrayList<Card>();
							for(Card handCard : thisPlayer.hand) {
								tempHand.add(handCard);
							}
							tempHand.add(piles.get(i).getPointCard());
							int score = calculateScore(tempHand, thisPlayer);
							if(score > highestPointCardScore) {
								highestPointCardScore = score;
								highestPointCardIndex = i;
							}
						}
					}
					if(piles.get(highestPointCardIndex).getPointCard() != null) {
						thisPlayer.hand.add(piles.get(highestPointCardIndex).buyPointCard());
					} else {
						choice = 1; //buy veggies instead
						emptyPiles = true;
					}
				} else if (choice == 1) {
					// TODO: Check what Veggies are available and run calculateScore to see which veggies are best to pick
					int cardsPicked = 0;
					for(Pile pile : piles) {
						if(pile.veggieCards[0] != null && cardsPicked < 2) {
							thisPlayer.hand.add(pile.buyVeggieCard(0));
							cardsPicked++;
						}
						if(pile.veggieCards[1] != null && cardsPicked < 2) {
							thisPlayer.hand.add(pile.buyVeggieCard(1));
							cardsPicked++;
						}
					}
					if(cardsPicked == 0 && !emptyPiles) {
						// Take a point card instead of veggies if there are no veggies left
						int highestPointCardIndex = 0;
						int highestPointCardScore = 0;
						for(int i = 0; i < piles.size(); i++) {
							if(piles.get(i).getPointCard() != null && piles.get(i).getPointCard().criteriaSideUp) {
								ArrayList<Card> tempHand = new ArrayList<Card>();
								for(Card handCard : thisPlayer.hand) {
									tempHand.add(handCard);
								}
								tempHand.add(piles.get(i).getPointCard());
								int score = calculateScore(tempHand, thisPlayer);
								if(score > highestPointCardScore) {
									highestPointCardScore = score;
									highestPointCardIndex = i;
								}
							}
						}
						if(piles.get(highestPointCardIndex).getPointCard() != null) {
							thisPlayer.hand.add(piles.get(highestPointCardIndex).buyPointCard());
						}
					}
				}
				sendToAllPlayers("Bot " + thisPlayer.playerID + "'s hand is now: \n"+displayHand(thisPlayer.hand)+"\n");
			}
			
			if(currentPlayer == players.size()-1) {
				currentPlayer = 0;
			} else {
				currentPlayer++;
			}
		}
		sendToAllPlayers(("\n-------------------------------------- CALCULATING SCORES --------------------------------------\n"));
		for(Player player : players) {
			sendToAllPlayers("Player " + player.playerID + "'s hand is: \n"+displayHand(player.hand));
			player.score = calculateScore(player.hand, player);
			sendToAllPlayers("\nPlayer " + player.playerID + "'s score is: " + player.score);
		}

		int maxScore = 0;
		int playerID = 0;
		for(Player player : players) {
			if(player.score > maxScore) {
				maxScore = player.score;
				playerID = player.playerID;
			}
		}
		for(Player player : players) {
			if(player.playerID == playerID) {
				player.sendMessage("\nCongratulations! You are the winner with a score of " + maxScore);
			} else {
				player.sendMessage("\nThe winner is player " + playerID + " with a score of " + maxScore);
			}
		}
	}

	public static void main(String[] args) {
		PointSalad game = new PointSalad(args);

	}
}