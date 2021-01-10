import { Queue } from "./queue";

export class StartingQueue extends Queue{
  constructor(x, y) {
    super(x,y);
    this.isStartingQueue = true;
    this.setupStartingQueue();
  } 

  setupStartingQueue() {
    this.fromConnectionPoint.removeSelf();
  }

  remove() { //starting queue cannot be deleted
    return; 
  }
}