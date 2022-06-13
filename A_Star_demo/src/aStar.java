// Importing input output classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class aStar {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	
	
	String[][] feld =	{{"1","1","1","1","1","1"}
						,{"1","1","1","1","1","1"}
						,{"1","1","3","1","E","1"}
						,{"1","1","1","1","1","1"}
						,{"1","1","1","1","1","1"}
						,{"*","1","1","1","1","1"}}; 
	Position startknoten;
	Position ziel;
	
	public class Position  implements Comparable<Position> {
		int x;
		int y;
		float priority;
		
		
		public Position(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int compareTo(Position b) {
			if(priority > b.getPriority()) {
				return 1;
			} else if(priority < b.getPriority()) {
				return -1;
			} else {
				return 0;
			}
			
		}
		
		@Override
		public boolean equals(Object obj) {
			return ((Position) obj).x == this.x && ((Position) obj).y == this.y;
		}

		public int getX() {
			return x;
		}
		
		public float getPriority() {
			return priority;
		}

		public void setPriority(float priority) {
			this.priority = priority;
		}

		public int getY() {
			return y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}
		
	}
	
	
	public List<Position> getUmliegendeKnoten(Position current){
		List<Position> myList = new ArrayList<Position>();
		int x = current.getX();
		int y = current.getY();
		
		 if (current.getX() > 0) {
			 if(!(startknoten.getX() == x-1 && startknoten.getY() == y) ) {
				 myList.add(new Position(current.getX() - 1, current.getY() ));
			 }
		}  
		 if (current.getX() < feld.length - 1) {
			 if(!(startknoten.getX() == x+1 && startknoten.getY() == y )) {
				 myList.add(new Position(current.getX() + 1, current.getY() ));
			 }
			
		}
		  if(current.getY() > 0 ) {
			  if(!(startknoten.getX() == x && startknoten.getY() == y-1 )) {
				 myList.add(new Position(current.getX(), current.getY()- 1 )); 
			  }
			
		} 
		if( current.getY() < (feld.length - 1)) {
			 if(!(startknoten.getX() == x && startknoten.getY() == y+1 )) {
				 myList.add(new Position(current.getX(), current.getY()+ 1 ));
			 }
			
		}
		
		return myList;
	}
	
	public aStar() {
		
	}
	
	public void init() {
		for (int i = 0; i < feld.length; i++) {
			for(int j = 0; j < feld[i].length; j++) {
				if (feld[i][j].equals("*")) {
					startknoten = new Position(i,j);
				}
				if (feld[i][j].equals("E")) {
					ziel = new Position(i,j);
				}
			}
		}
	}
	
	public void Star() {
		 init();
		 
		 PriorityQueue<Position> openList = new PriorityQueue<Position>();
		 Map<Position, Position> vorganger = new HashMap<Position, Position>();
		 Map<Position, Float> bKosten = new HashMap<Position, Float>();
		 
		 startknoten.setPriority(0);
		 openList.add(startknoten);
		 vorganger.put(startknoten, null);
		 bKosten.put(startknoten,  0.0f);
		  
		 while(!openList.isEmpty()) {
			  
			  Position current = openList.poll();
			  

			  
			  if(current.equals(ziel)) {
				  ziel = current;
				  break;
			  }
			  
			  List<Position> surrounding = getUmliegendeKnoten(current);
			  
			  for(Position next : surrounding) { // Expandieren
				  
				 float neueKosten = bKosten.get(current) + kostenNaechstePosition(current, next); // g(x)
				 	
				 	if( !bKosten.containsKey(next) || neueKosten < bKosten.get(next) ) { 
				 		bKosten.put(next, neueKosten);
				 		float h = heuristic(next, ziel); // h(x)
				 		float priority = neueKosten + h; // f(x)
				 		next.setPriority(priority);
				 		openList.add(next);
				 		vorganger.put(next, current);
				 	}	 
			  }  
			  
		  }
		  printFeld(vorganger, bKosten);
		  
	}
	
	public float kostenNaechstePosition(Position current, Position next) {
		String inhalt = feld[next.getX()][next.getY()];
		if(inhalt == "E" || inhalt == "*") {
			return 0;
		}
		float preis = Integer.parseInt(inhalt);
		return preis;
	}
	
	public float heuristic (Position p, Position ziel) { // Euklidischer Abstand
		double dx = Math.pow( ( p.getX()- ziel.getX() ), 2 );
		double dy = Math.pow( ( p.getY() - ziel.getY() ), 2);
		return (float) Math.sqrt(( dx + dy )) ; 
	}
	
	public void printFeld() {
		
		for (String[] row : feld) {
			for (String colum : row) {
				printInColor(colum);
				System.out.print("\t");
			}	
			System.out.println();
		}
		System.out.println("\n");
	} 
	
	public void printFeld(Map<Position, Position> path, Map<Position, Float> cost) {
		String[][] localfeld = feld;
		Position p1;
		printFeld();
		
		p1 = path.get(ziel);
		localfeld[p1.getX()][p1.getY()] = "@";
		
		while(p1 != startknoten) {
			p1 = path.get(p1);
			localfeld[p1.getX()][p1.getY()] = "@";
		} 
		
		for (String[] row : localfeld) {
			for (String colum : row) {
				printInColor(colum);
				System.out.print("\t");
			}	
			System.out.println();
		}
		
	} 
	
	
	private void printInColor(String element) {
		if(element.equals("*")) {
			System.out.print(ANSI_YELLOW +element + ANSI_RESET + "\t" );
		} else if(element.equals("E")) {System.out.print(ANSI_RED + element + ANSI_RESET +"\t" );}
		
		else {
			System.out.print(element);
		}
		
	}

}
