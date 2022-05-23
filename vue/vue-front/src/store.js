import Vue from 'vue';
import Vuex from 'vuex';
import axios from "axios"
Vue.use(Vuex);

export const SET_WINNER = 'SET_WINNER';
export const SET_LOGIN_CHECK = 'SET_LOGIN_CHECK';
export const SET_INITIAL_DATA = 'SET_INITIAL_DATA';
export const SET_NOTICE_DATA = 'SET_NOTICE_DATA';
export const SET_TASK_DATA = 'SET_TASK_DATA';
export const SET_LECTURES_DATA = 'SET_LECTURES_DATA';
export const INCREASE_COMPLETED_TASK = 'INCREASE_COMPLETED_TASK';
export const INCREASE_INCOMPLETED_TASK = 'INCREASE_INCOMPLETED_TASK';
export const CRAWL_SUBJECT = 'crawlSubject';

const api = axios.create({baseURL: 'http://localhost:38080'});

export default new Vuex.Store({
    state: {
        loginCheck: false,
        loginLoading: false,
        studentName: '',
        studentNumber: '',
        subjectCardData: [],
        completedTask: 0,
        incompletedTask: 0,
    }, //Vue에 data와 비슷
    getters: {
        totalTask(state) {
            return state.incompletedTask + state.completedTask;
        },
    },//Vue의 computed와 비슷 
    //완전히 같은게 아니라 비슷하다는 것 명심
    mutations: {
        [SET_WINNER](state, winner) {
            state.winner = winner;
        },
        [SET_LOGIN_CHECK](state, loginCheck) {
            state.loginCheck = loginCheck;
        },
        [SET_INITIAL_DATA](state, {studentName: name, studentNumber: number, subjectCardData: cardData}) {
            state.studentName = name;
            state.studentNumber = number;
            // state.subjectCardData = cardData;
            let objs = cardData;
            for (let i = 0; i < objs.length; i++) {
                const element = objs[i];
                element['isCrawling'] = [true, true, true];
                element['lectures'] = [];
                element['notice'] = [];
                element['task'] = [];
            }
            // console.log(objs);
            state.subjectCardData = objs;
        },
        [INCREASE_INCOMPLETED_TASK](state) {
            state.incompletedTask += 1;
        },
        [INCREASE_COMPLETED_TASK](state) {
            state.completedTask += 1;
        },
        [SET_LECTURES_DATA](state, {cardIndex: index, lecturesData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].lectures[i] = { 
                        lectureWeekId: data[i].lectureWeekId, 
                        title: data[i].title, 
                        endDate: data[i].endDate, 
                        startDate: data[i].startDate, 
                        lectures: data[i].lectures, 
                    };
                }
            }
            else {
                state.subjectCardData[index].lectures = null;
            }
            Vue.set(state.subjectCardData[index].isCrawling, 0, false);
            // console.log(state.subjectCardData[index].lectures);
        },
        [SET_NOTICE_DATA](state, {cardIndex: index, noticeData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].notice[i] = { 
                        noticeId: data[i].noticeId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        author: data[i].author, 
                        createDate: data[i].createDate
                    };
                }
            }
            else {
                state.subjectCardData[index].notice = null;
            }
            Vue.set(state.subjectCardData[index].isCrawling, 1, false);
        },
        [SET_TASK_DATA](state, {cardIndex: index, taskData: data}) {
            if(data.length != 0) {
                for(let i = 0; i < data.length; i++) {
                    state.subjectCardData[index].task[i] = { 
                        taskId: data[i].taskId, 
                        title: data[i].title, 
                        description: data[i].description, 
                        startDate: data[i].startDate, 
                        endDate: data[i].endDate, 
                        notSubmittedNum: data[i].notSubmittedNum, 
                        submissionNum: data[i].submissionNum, 
                        totalNum: data[i].totalNum, 
                        submitYN: data[i].submitYN, 
                    };
                    if(state.subjectCardData[index].task[i].submitYN == 'Y') {
                        state.incompletedTask += 1;
                    } else if(state.subjectCardData[index].task[i].submitYN == 'N') {
                        state.completedTask += 1;
                    }
                }
            }
            else {
                state.subjectCardData[index].task = null;
            }
            Vue.set(state.subjectCardData[index].isCrawling, 2, false);
            // console.log(state.subjectCardData[index].task);
        },
    }, //state를 동기적으로 수정할 때 사용
    //state를 바꿀때 바로 바꾸지말고 mutations를 통해 바꾸길 권장
    actions: {
        async crawlSubject(context) {
            for(let i = 0; i < this.state.subjectCardData.length; i++) {
                await api.post('/crawlLecture', null, {params: {
                    subjectId: this.state.subjectCardData[i].subjectId,
                }}).then((response) => {
                    // console.log(response.data);
                    context.commit([SET_LECTURES_DATA], {cardIndex: i, lecturesData: response.data});
                });
                await api.post('/crawlNotice', null, {params: {
                    subjectId: this.state.subjectCardData[i].subjectId,
                }}).then((response) => {
                    // console.log(response.data);
                    context.commit([SET_NOTICE_DATA], {cardIndex: i, noticeData: response.data});
                });
                await api.post('/crawlTask', null, {params: {
                    subjectId: this.state.subjectCardData[i].subjectId,
                }}).then((response) => {
                    // console.log(response.data);
                    context.commit([SET_TASK_DATA], {cardIndex: i, taskData: response.data});
                });

            }
        }
    }, // 비동기를 사용할 때, 또는 여러 뮤테이션을 연달아 실행할 때
});
