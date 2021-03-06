
public class Board{
  
  private int[][] board;
  private static int[][] goal;
  private static int dimension;
  private static Queue<Board> q = new Queue<Board>();
 
//FINISHED
    public Board(int[][] blocks){ //constuct a board from an N by N array of blocks // where blocks[i][j] = block in row i, column j

      dimension = blocks.length;
       
       board = new int[dimension][dimension];
       
       //deep copy those blocks
       for (int x=0; x<dimension; x++){
         for (int y=0; y<dimension; y++){
           board[x][y] = blocks[x][y];
         }
       }
      
    }
    
    public static void primeGoal(){
      //create goal board
       goal = new int[dimension][dimension];
       int value = 1; 
       for (int i=0; i<dimension; i++){
         for (int j=0; j<dimension; j++){
             goal[i][j] = value;
             value++; 
         }
       }
       goal[dimension-1][dimension-1] = 0; //set '*'
    }
      
 //FINISHED
    public int dimension(){ //board dimension N
       return dimension;
    }

//FINISHED
    public int hamming(){ //number of blocks out of place
      int result = 0;
      
      for (int i=0; i<dimension; i++){
        for (int j=0; j<dimension; j++){
          if (board[i][j] != 0 && board[i][j] != goal[i][j]){ //(ignore 0) //find out of place
            result++; //if there is a block out of place, add it to the result
          }
        }
      }
      
      return result;
    }
    
    
//FINISHED
    public int manhattan(){ //sum of manhattan distances between blocks and goal
      int result = 0;
      
      for (int b_x=0; b_x<dimension; b_x++){
        for (int b_y=0; b_y<dimension; b_y++){
          if (board[b_x][b_y] != 0 && board[b_x][b_y] != goal[b_x][b_y]){ //if we hit an out-of-place //(ignore 0)
            
             int value = board[b_x][b_y]; //save value to search for
             
             //search for correct position
             int g_x=0;
             int g_y=0;
             SEARCH:for (g_x = 0; g_x<dimension; g_x++){
               for (g_y = 0; g_y<dimension; g_y++){
                 if (value == goal[g_x][g_y]){ //we found the proper position
                   break SEARCH; //break search, proper position is at g_x and g_y
                 }
               }
             }//end SEARCH
        
            //now we have proper position in g_x and g_y
            int diff = Math.abs(b_x - g_x) + Math.abs(b_y - g_y); //find distance
            
            result += diff;//add dif's to result
        }//end if !=
      }
    }//end topmost for
      
      return result;
 }

//FINISHED
    public boolean isGoal(){ //is this board the goal board?
      boolean result = true;
      
      //iterate through each element of board and compare to goal
      FOR: for (int i = 0; i < dimension; i++){
             for (int j = 0; j < dimension; j++){
               if (board[i][j] != goal[i][j]){
                 result = false;
                 break FOR; //shortcircuit
               }
             }
           }
      
      return result;
    }
    
 //FINISHED
    //To be solveable:
    //If dimension is odd, number of inversions must be Even
    //If dimension is even, number of inversions + row of blank must be Odd
    public boolean isSolvable(){ //is the board solvable?

      int inversions = 0; 
      
      //Get number of inversions
      for (int num = 1; num < ((dimension*dimension)-1); num++){ 

        NEXTNUM:for (int row = 0; row < dimension; row++){
          for (int col = 0; col < dimension; col++){
            //for each index, 
            if (board[row][col] > num){
              inversions++;
            }
            if (board[row][col] == num){ //SHORT CIRCUIT
              break NEXTNUM;
            }
          }
        }
        
      }
      
      //Odd/even
      if (dimension%2 == 1) { //if dimension is ODD
        //inversions must be even
        if (inversions%2 == 0){ //even = solvable!
          return true; //RETURN
        } else { //odd, unsolvable!
          System.out.print("Unsolvable: ");
          return false;
        }
          
      } else { //dimension is EVEN!
        //Find row of blank
        int blank_row = 0;
        LOOP:for (int r = 0; r<dimension; r++){
          for(int c = 0; c<dimension;c++){
            if (board[r][c] == 0){ //FOUND!
              blank_row = r;
              break LOOP;
            }
          }
        }
        //Add into inversions
        inversions += blank_row;
        
        //Now, this number must be odd to be solvable
        if(inversions%2 == 1){//odd, solvable!
          return true;
        } else {//even, unsolvable
          System.out.print("Unsolvable: ");
          return false;
        }
      
      }

    }
    
//FINISHED
    public boolean equals(Object y){ //does this board equal y?
      boolean result = true;
      
      if (y instanceof Board){
        Board tmp = (Board)y;
        
        LOOP:for (int row = 0; row < dimension; row++){
          for (int col= 0; col < dimension;col++){
            if (tmp.board[row][col] != this.board[row][col]){
              result = false;
              break LOOP;
            }
          }
        }

        
      } else {
        result = false;
      }
      
      return result;
    }
    
//FINISHED
    public Iterable<Board> neighbors(){ //place all neighboring boards into your iterable Queue (assignment 1)
      while(!q.isEmpty()){
        q.dequeue();
      }

      int row = 0, col = 0;
      
      //Search for the asterisk
      LOOP:for (row = 0;row<dimension;row++){
        for (col = 0;col<dimension;col++){
          if (board[row][col] == 0){ //found!
            break LOOP;
          }
        }
      } //end LOOP
      
      //We now have the location of the asterisk
      //Let's be careful if it is on the edge of the 2d array:

      if (col != 0){
        //swapLeft
        Board temp = new Board(this.board); //create temp object //dont want to edit the original board
        temp.board[row][col] = this.board[row][col-1];
        temp.board[row][col-1] = 0;
        //add this temp to q
        q.enqueue(temp);
      }
      
      if (col != dimension-1){
        //swapRight
        Board temp = new Board(this.board); //create temp object //dont want to edit the original board
        temp.board[row][col] = this.board[row][col+1];
        temp.board[row][col+1] = 0;
        //add this temp to q
        q.enqueue(temp);
      }
      if (row != 0){
        //swapNorth
        Board temp = new Board(this.board); //create temp object //dont want to edit the original board
        temp.board[row][col] = this.board[row-1][col];
        temp.board[row-1][col] = 0;
        //add this temp to q
        q.enqueue(temp);
      }
      if (row != dimension-1){
        //swapSouth
        Board temp = new Board(this.board); //create temp object //dont want to edit the original board
        temp.board[row][col] = this.board[row+1][col];
        temp.board[row+1][col] = 0;
        //add this temp to q
        q.enqueue(temp);
      }
      
      return q;
    }
    
 //FINISHED
    public String toString(){ //string representation of the board (in the output format specified below)
      StringBuilder result = new StringBuilder();
      
      result.append(dimension + "\n");
      
      for (int i = 0; i < dimension; i++){
        for (int j = 0; j < dimension; j++){
          if(board[i][j] == 0){
              result.append(" " + "*");
          } else {
            result.append(" " + board[i][j]);
          }
        }
        result.append("\n");
      }
      
      return result.toString();
    }
    
    
}