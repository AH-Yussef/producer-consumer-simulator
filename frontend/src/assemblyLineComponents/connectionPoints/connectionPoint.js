import gsap from 'gsap';

export class ConnectionPoint {
  constructor(component) {
    this.componentAttached = component;
    this.connectionPoint = null;
    this.connectionPointWrapper = null;
  }

  removeSelf() {
    this.connectionPoint.remove();
    this.connectionPointWrapper.remove();
  }

  _createConnectionPoint(centerX){
    const board = document.getElementById("board");
    const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
    const connectionPoint = document.createElementNS(svgns, "circle");
    gsap.set(connectionPoint, {
      attr: {
        cx: centerX, cy: this.componentAttached.center.y, r: 6,
        fill: 'black', 'fill-opacity': 1 ,stroke: "none",'stroke-width': 0,
      },
      css: {
        userSelect: 'none',
      }
    });
    board.appendChild(connectionPoint);

    this.connectionPoint = connectionPoint;
  }

  _createConnectionWrapper(centerX) {
    const board = document.getElementById("board");
    const svgns = "http://www.w3.org/2000/svg"; //variable for the namespace
    const connectionWrapper = document.createElementNS(svgns, "circle");
    gsap.set(connectionWrapper, {
      attr: {
        cx: centerX, cy: this.componentAttached.center.y, r: 12,
        fill: 'transparent','fill-opacity': 0, stroke: "none",'stroke-width': 0,
      },
      css: {
        userSelect: "none",
      }
    });
    board.appendChild(connectionWrapper);

    this.connectionPointWrapper = connectionWrapper;
  }
}