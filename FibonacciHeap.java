import java.util.Arrays;


/**
 * FibonacciHeap
 *
 *
 *Inbal Avivi  inbalavivi 316266121
 *Noy Cohen noy1 205476542
 *
 * An implementation of fibonacci heap over non-negative integers.
 */
	public class FibonacciHeap  
	{
		public HeapNode min;
		public int size;
		public int marked;
		public int numOfTrees;
		public static int Links;      		
		public static int Cuts;
		
		

	   /**
		* public boolean empty()
		*
		* precondition: none
		* 
		* The method returns true if and only if the heap
		* is empty.
		*   
		*/
	    public boolean empty()
	    {
	    	return min==null;
	    }
			
	   /**
	    * public HeapNode insert(int key)
	    *
	    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
	    */
	    public HeapNode insert(int key)
	    {
	    	HeapNode node=new HeapNode(key);
	    	if(empty()) {
	    		min=node;
	    	}
	    	else {
	    		insertNode(node);
	        	if(key<min.getKey()) {
	        		min=node;
	        	}
	    	}
	    	size++;
	    	numOfTrees++;
	    	return node; 
	    }
	    /**
	     * public void insertNode(HeapNode newNode)
	     * the function receives a @param newNode and insert it into the heap, left to the previous minimum.
	     * 
	     */
	    public void insertNode(HeapNode newNode)
	    {
	    	newNode.ifdelete=false;
	    	if (min.next==min) { //if the node min has no siblings
	    		 newNode.next=min;
	  	  		 newNode.prev=min;
	  	  		 min.next=newNode;
	  	  		 min.prev=newNode;
   	  		 }
   	  		 else {
	   	  		min.prev.setNext(newNode);
	 	  		min.setPrev(newNode);
   	  		 }	  
	    }
	    
	    
	   /**
	    * public void deleteMin()
	    *
	    * Delete the node containing the minimum key.
	    *
	    */
	    public void deleteMin()
	    {
	    	if(!this.empty()) {
		    	numOfTrees=numOfTrees+min.rank-1;
		    	min.ifdelete=true; //now min is deleted
		    	if(size==1) { //if the heap bacome empty
		    		size--;
			    	min=null;
		    	}
		    	else {
		    		size--;
		    		if(min.next!=min) { //min has siblings
			    		HeapNode lastMin=min;
			    		if(min.child==null) { //min does not have child
			    			min=min.next;
			    		}
			    		else {				//has child
			    			HeapNode childPrev=min.child.prev;
			    			min=min.child;
			    			lastMin.prev.setNext(min);
			    			childPrev.setNext(lastMin.next);
			    			deletePar();
			    		}
			    		lastMin.prev.setNext(lastMin.next);
				  		lastMin.next.setPrev(lastMin.prev);
				  		SuccessiveLinking();
			    	}
			    	else { //min does not have siblings and has a child
			    		min=min.child;
			    		deletePar();
			    		SuccessiveLinking();
			    	}
		    	}	
	    	}
	    }
	    
	    /**
	     * public void deletePar()
	     * when we delete the minimum we need to change all his children's parent to null.
	     * after the deletion the children become root.
	     */
	    public void deletePar() {
	    	min.parent=null;
    		HeapNode x=min.next;
    		while(x!=min) {
    			x.parent=null;
    			x=x.next;
    		}
	    }
	    /**
	     * public HeapNode DiconnectNode(HeapNode root)
	     * the function receives @param root and disconnect it from the heap.
	     * @return HeapNode without parent, previous node and next node.
	     */
	    public HeapNode DiconnectNode(HeapNode root) {
	    	root.parent=null;
	    	root.prev.setNext(root.next);
	    	root.setPrev(root);
	    	return root;
	    }
 
	    /**
	     * public void SuccessiveLinking()
	     * the function merges trees with equal rank.
	     * at the end of the process the heap will contain at most one tree of every rank and at most logn trees.
	     */
	    public void SuccessiveLinking(){
	    	HeapNode curr=this.min;
	    	HeapNode[] array=new HeapNode[(int)(2*Math.log(size()) / Math.log(2))+1];
	    	while(curr.next!=curr) { //we disconnect the node from heap. the while stop when there is no sibling to the node in heap.
	    		HeapNode next =curr.next;
	    		HeapNode root=DiconnectNode(curr);
	    		SuccessiveLinkingRec(array,root); //sending the disconnected root for the recursive function
	    		curr=next;
	    	}
	    	SuccessiveLinkingRec(array,curr); //the last node that already disconnect.
			buildHeap(array);
		}
	    
	    /**
	     * public void SuccessiveLinkingRec(HeapNode[] array, HeapNode curr)
	     * the function add @param curr to the @param array by his rank.
	     * if the array[rank of curr] is occupied, the function call function linking to link the two nodes and place them in array by there new rank.
	     * 
	     */
	    public void SuccessiveLinkingRec(HeapNode[] array, HeapNode curr) {
	    	if(array[curr.rank]!=null) { // array[rank of curr] is occupied
	    		HeapNode inArray =array[curr.rank]; //save the node that already in array
	    		array[curr.rank]=null; //clean the cell
	    		HeapNode newTree=linking(curr,inArray);
	    		SuccessiveLinkingRec(array,newTree);
			}
	    	else { // array[rank of curr] is vacant, so we add curr to array.
	    		array[curr.rank]=curr;
	    	}
	    }
	    
	    /**
	     * public HeapNode linking(HeapNode root1, HeapNode root2)
	     * the function link two roots with the same rank - @param root1, @param root2,.
	     * @return linked HeapNode that his root is the minimum between @param root1 and @param root2.
	     */
	    public HeapNode linking(HeapNode root1, HeapNode root2) {
	    	Links++;  
	    	if (root1.key < root2.key) { //if root1 is the minimum.
	    		if(root1.rank==0) { //root1 has no other children between root1 and root2
					root1.setChild(root2);
					root1.rank++;
					return root1;
				}
				else { //root1 has old children			
					HeapNode oldChild=root1.getChild();
					root1.setChild(root2);
					root2.setPrev(oldChild.prev);
					root2.setNext(oldChild);
					root1.rank++;
					return root1;
				}
	    	}
	    	else { // if root2 is the minimum between root1 and root2
	    		if(root2.rank==0) {	//root2 has no children
					root2.setChild(root1);
					root2.rank++;
					return root2;
				}
				else {		//root2 has old children		
					HeapNode oldChild=root2.child;
					root2.setChild(root1);
					root1.setPrev(oldChild.prev);
					root1.setNext(oldChild);
					root2.rank++;
					return root2;
				}
	    	}
	    }
	    
	    /**
	     * public void buildHeap(HeapNode[] array)
	     * @param array is a array of HeapNodes sort by their rank, at most one node for each size, the array size is 2*logn.
	     * the function make a heap from @param array, find the minimum of the heap and count the number of trees in the new heap.
	     */
	    public void buildHeap(HeapNode[] array) {
	    	numOfTrees=1;
	    	for (HeapNode node : array) { //find new minimum of the heap
	    		if (node!=null) {
		    		if(node.getKey()<min.getKey()) {
		    			this.min=node;
		    		}
	    		}
	    	}
	    	HeapNode curr=min;
	    	for (HeapNode node : array) { 
				if (node!=null && node!=min) { //connecting the nodes starting from the minimum of the heap.
					curr.setNext(node);
					curr=node;
					numOfTrees++;
				}
	    	}	
	    	curr.setNext(min); //the last one connecting the minimum of the heap to make circular heap.
	    }

	   /**
	    * public HeapNode findMin()
	    *
	    * Return the node of the heap whose key is minimal. 
	    *
	    */
	    public HeapNode findMin()
	    {
	    	if(empty()) {
	    		return null;
	    	}
	    	return this.min;
	    } 
	    
	   /**
	    * public void meld (FibonacciHeap heap2)
	    *
	    * Meld the heap with heap2
	    *
	    */
	    public void meld (FibonacciHeap heap2)
	    {
	    	this.size = this.size+ heap2.size;
	    	numOfTrees = this.numOfTrees+ heap2.numOfTrees;
	    	marked = this.marked + heap2.marked;
	    	if(this.empty()&&!heap2.empty()) {
	    		min=heap2.min;
	    	}
	    	else if(!this.empty() && !heap2.empty()) {
		    	if (this.min.getKey()>heap2.findMin().getKey()) {
		    		HeapNode lastmin= this.min;
		    		HeapNode lastminPrev= lastmin.prev;
		    		min=heap2.findMin();
		    		min.prev.setNext(lastmin);
		    		lastminPrev.setNext(min);
		    	}
		    	else {
		    		HeapNode heap2min = heap2.findMin();
		    		HeapNode heap2minPrev = heap2min.prev;
		    		min.prev.setNext(heap2min);
		    		heap2minPrev.setNext(min);
		    	}
	    	}
	    }

	   /**
	    * public int size()
	    *
	    * Return the number of elements in the heap
	    *   
	    */
	    public int size()
	    {
	    	return size; 
	    }
	    	
	    /**
	    * public int[] countersRep()
	    *
	    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
	    * 
	    */
	    public int[] countersRep() {
	    	if(empty()) {
	    		int[] emptyarr=new int[0];
	    		return emptyarr;
	    	}
	    	int MaxRank=min.getRank();
		    int[] arr=new int[(int)(Math.log(size()) / Math.log(2))+2];
		    arr[min.getRank()]=arr[min.rank]+1;
		    if (min.next==min) { //min has no sibling
		    	MaxRank=min.rank;
		    }
		    HeapNode curr = min.next;
		    while (curr!=min) {
		    	arr[curr.getRank()]=arr[curr.rank]+1;;
		    	if(curr.getRank()>MaxRank) {
		    		MaxRank=curr.getRank();
		    	}
		    	curr=curr.next;
		     }
		     arr = Arrays.copyOfRange(arr, 0, MaxRank+1);
		     return arr;
		  }
			
	   /**
	    * public void delete(HeapNode x)
	    *
	    * Deletes the node x from the heap. 
	    *
	    */
	    public void delete(HeapNode x) 
	    {
	    	if(!x.ifdelete) {
	    		decreaseKey(x, x.getKey()+1);
	    		deleteMin();
	    	}
	    }

	   /**
	    * public void decreaseKey(HeapNode x, int delta)
	    *
	    * The function decreases the key of the node x by delta. The structure of the heap should be updated
	    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
	    */
	    public void decreaseKey(HeapNode x, int delta)
	    {
	    	if(!x.ifdelete) {
		    	x.SetKey(x.getKey()-delta);
		    	if(x.parent!=null) {
		    		if(x.key<x.parent.key) { //the decrease key change the order of the heap
		    			HeapNode parent=x.parent;
		    			cut(x);
		    			if(parent.parent!=null) { //the parent is not the root 
		    				if(parent.mark) {
			    				cascadingCut(parent);	
			    			}
		    				else {
			    				parent.mark=true;
					    		marked++;
			    			}
		    			}
		    		}
		    	}
		    	if(x.getKey()<min.getKey()) { //update the minimum if necessary
		    		min=x;
		    	}
	    	}
	    }
	    

	    /**
	     * public void cut(HeapNode node)
	     * the function cut @param node from his tree, and connect it to the heap
	     */
	    public void cut(HeapNode node) {
	    	Cuts++;       
	    	numOfTrees++;
	    	node.parent.rank--;
	    	if(node.mark) {
		    	marked--;
		    	node.mark=false;
	    	}
	    	if(node.parent.child==node) { //node is the main child
	    		if(node.next==node) { //node is the only child
	    			node.parent.child=null;
	    			node.parent=null;
	    			//insertNode(node);
	    		}
	    		else { //node is the main child but have sibling 
	    			node.parent.child=node.next;
	    			node.parent=null;
	    	    	node.prev.setNext(node.next);
	    	    	node.prev=node;
	    			//insertNode(node);
	    		}
	    	}
	    	else { //node is not the main child
	    		node.parent=null;
		    	node.prev.setNext(node.next);
		    	node.prev=node;
	    		//insertNode(node);
	    	}
	    	insertNode(node);
	    }
	    

	    /**
	     * public void cascadingCut(HeapNode node) 
	     * the function receive @param node and if the node is mark the function cut the node from the tree and check his parent.
	     * the function stop at the first unmark node or a root.
	     * if the function stop at unmark node that is not a root, the function mark the node.
	     */
	    public void cascadingCut(HeapNode node) {
	    	while(node!=null && node.mark) {
	    		HeapNode par = node.parent;
	    		cut(node);
	    		node=par;	
	    	}
	    	if (node!=null && node.parent!=null) { //not a root
	    		node.mark=true;
	    		marked++;
	    	} 

	    }
	    	
	   /**
	    * public int potential() 
	    *
	    * This function returns the current potential of the heap, which is:
	    * Potential = #trees + 2*#marked
	    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
	    */
	    public int potential() 
	    {    
	    	return 2*marked+numOfTrees;
	    }

	   /**
	    * public static int totalLinks() 
	    *
	    * This static function returns the total number of link operations made during the run-time of the program.
	    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
	    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
	    * in its root.
	    */
	    public static int totalLinks() {     
	    	return Links; 
	    }

	   /**
	    * public static int totalCuts() 
	    *
	    * This static function returns the total number of cut operations made during the run-time of the program.
	    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
	    */
	    public static int totalCuts() {    
	    	return Cuts;
	    }
	    
	   /**
	    * public class HeapNode
	    * 
	    * If you wish to implement classes other than FibonacciHeap
	    * (for example HeapNode), do it in this file, not in 
	    * another file 
	    *  
	    */
	    public class HeapNode {

		public int key;
		public int rank;
		public boolean mark;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public boolean ifdelete; // checks if the node is deleted from the heap.
		

		/**
		 * public HeapNode(int key)
		 * Constructor of heap node with key @param key
		 * the new heap node have no parent or child, and the node next and prev points to itself.
		 */
	  	public HeapNode(int key) {
		    this.key = key;
		    this.mark=false;
		    this.child=null;
		    this.parent=null;
		    this.prev=this;
		    this.next=this;
		    this.ifdelete = false;
		    
	      }

	  	/**
	  	 * public int getKey()
	  	 * @return the key of the node.
	  	 */
		public int getKey() {
		    return this.key;
	      }
		/**
		 * public void SetKey(int i)
		 * set the key of the node to int @param i
		 */
	  	public void SetKey(int i) {
			this.key=i;
		}

	  	/**
	  	 * public int getRank()
	  	 * @return the rank of the node.
	  	 */
		public int getRank() {
			return this.rank;
		}

		/**
		 * public void setChild(HeapNode child)
		 * set the child of the current node to be the node @param child
		 */
	  	public void setChild(HeapNode child) {
	  		this.child=child;
	  		child.parent=this;
	  	 }
	  	
	  	/**
	  	 * public HeapNode getChild()
	  	 * @return the child of the node.
	  	 */
		public HeapNode getChild() {
			return this.child;
		} 
	  
		/**
		 * public void setNext(HeapNode node)
		 * change the next pointer of the current node to @param node and the @param node previous pointer to be the current node.
		 */
	   public void setNext(HeapNode node) {
		   this.next=node;
		   node.prev=this;
	   }
	  	 
	   /**
		 * public void setPrev(HeapNode node)
		 * change the previous pointer of the current node to @param node and the @param node next pointer to be the current node.
		 */
	  	public void setPrev(HeapNode node) {
	  		this.prev=node;
	  		node.next=this;
	  	}
	  	
	  }
	    
		public void printHeap() {
		String s="";
		HeapNode node=min;
		for(int i=0;i<numOfTrees;i++) {
			
			s+= "(" + node.getKey() + "," + node.rank + ") "; 
			node=node.next;
			
		}
		
		System.out.println(s);
	}
}	
