const state = {
  drawingMode: false,
  componentType: null,
  currentComponenet: null,
	queues: new Map(),
	machines: new Map()
};
  
const getters = {
  isDrawing: state => state.drawingMode,
  componentType: state => state.componentType,
	queues: state => state.queues,
  machines: state => state.machines,
  getCurrentComponent: state => state.currentComponenet,
};
  
const actions = {
	enableDrawingMode: ({commit}) => commit('setDrawingMode', true),
	disableDrawingMode: ({commit}) => commit('setDrawingMode', false),
  setComponentType: ({commit}, componentType) => commit('changeComponentType', componentType),
  setCurrentComponent: ({commit}, currentComponenet) => commit('changeCurrentComponent', currentComponenet),
	pushNewQueue: ({commit}, queue) => { 
		commit('pushQueue', queue);
		for(let queue of state.queues.values()){
			queue.resetCount();
		}
		document.getElementById("replay-btn").className = 'hidden'; 
	},
	pushNewMachine: ({commit}, machine) => {
		commit('pushMachine', machine);
		for(let machine of state.machines.values()){
			machine.resetColor();
		}
		document.getElementById("replay-btn").className = 'hidden'; 
	},
	clearAllQueues: ({commit}) => commit('clearQueues'),    
  clearAllMachines: ({commit}) => commit('clearMachines'),    
}
  
const mutations = {
	setDrawingMode: (state, drawingMode) => state.drawingMode = drawingMode,
  changeComponentType: (state, componentType) => state.componentType = componentType,
  changeCurrentComponent: (state, currentComponenet) => state.currentComponenet = currentComponenet,
	pushQueue: (state, queue) => state.queues.set(queue.code, queue),
	pushMachine: (state, machine) => state.machines.set(machine.code, machine),
	clearQueues : (state) => state.queues.length = 0,    
	clearMachines : (state) => state.machines.length = 0,    
};
  
export default{
	state,
	getters,
	actions,
	mutations,
};