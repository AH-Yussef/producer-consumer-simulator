const state = {
  boardMouseDown: false,

  fromComponent: null,
  toComponent: null,

  currSelectedComponent: null,

  errorMsg: "",
  errorCard: null,
};
  
const getters = {
  isBoardMouseDown: state => state.boardMouseDown,
  getFromComponent: state => state.fromComponent,
  getToComponent: state => state.toComponent,
  getCurrSelectedComponent: state => state.currSelectedComponent,
  getErrorMsg: state => state.errorMsg,
  getErrorCard: state => state.errorCard,
};
  
const actions = {
  setBoardMouseDown: ({commit}, status) => commit('changeBoardMouseDown', status),

  setFromComponent: ({commit}, fromComponent) => commit('changeFromComponent', fromComponent),
  setToComponent: ({commit}, toComponent) => {
    if(state.fromComponent == null) return;
    commit('changeToComponent', toComponent);
  },
  
  setErrorMsg: ({commit}, msg) => commit('changeErrorMsg', msg),
  setErrorCard: ({commit}, card) => commit('changeErrorCard', card),

  resetFromComponent: ({commit}) => commit('changeFromComponent', null),
  resetToComponent: ({commit}) => commit('changeToComponent', null),
  resetCurrSelectedComponent: ({commit}) => commit('setCurrSelectedComponent', null),

}
  
const mutations = {
  changeBoardMouseDown: (state, status) => state.boardMouseDown = status,
  changeFromComponent: (state, fromComponent) => state.fromComponent = fromComponent,
  changeToComponent: (state, toComponent) => state.toComponent = toComponent,

  setCurrSelectedComponent: (state, component) => state.currSelectedComponent = component,

  changeErrorCard: (state, card) => state.errorCard = card,
  changeErrorMsg: (state, msg) => state.errorMsg = msg,
};
  
export default{
  state,
  getters,
  actions,
  mutations,
};