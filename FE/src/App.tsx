"use client"
import axios from "axios"
import { useState, useEffect } from "react";

axios.defaults.baseURL="http://localhost:8080";
axios.defaults.headers.post['Content-Type'] = 'application/json';

let accessToken = ""
let refreshToken = ""

axios.interceptors.response.use(
  (response) => {
    accessToken = response.headers["access_token"];
    refreshToken = response.headers["refresh_token"];
    return response;
  },
  async (error) => {
    if(error.response?.status == 401){
      error.config.headers = {
        Refresh_Token : refreshToken
      }

      const response = await axios.request(error.config)
      return response;
    }
    return Promise.reject(error);
  }
)

axios.interceptors.request.use(
  (config) => {
    config.headers["access_token"] = accessToken;
    return config;
  }
)

export default function App(){
  const [loginInput, setLoginInput] = useState({
    id : "",
    password : ""
  })

  const [studentList, setStudentList] = useState([])
  const [workbookList, setWorkbookList] = useState([])

  async function getWorkbooks(){
    const {data} = await axios.get("/api/workbook")
    setWorkbookList(data)
  }

  async function getStudents(){
    const {data} = await axios.get("/api/student")
    setStudentList(data)
  }

  function handleInputChange(field : any, event : any){
    setLoginInput((prevState) => ({
      ...prevState,
      [field] : event.target.value,
    }));
  }

  async function loginStart(event : any){
    event.preventDefault()
    console.log("login data = ",loginInput)
    try{
      await axios.post("/api/login",loginInput, {withCredentials : true})
    }catch(e){
      console.log(e)
    }
  }

  return (
    <>
      <div>
        로그인
        <form onSubmit={loginStart}>
          <input type="text" id="id" name="id" placeholder="아이디를 입력해주세요." onChange={(event) => handleInputChange("id", event)} />
          <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요." onChange={(event) => handleInputChange("password", event)}/>
          <input type="submit" />
        </form>

        <div onClick={getStudents}>
          학생 불러오기
        </div>

        <div>
          {studentList.map((student) =>(
            <a href="#">{student.name} <br /></a>
          ))}
        </div>

        <div onClick={getWorkbooks}>문제집 불러오기</div>

        <div>
          {workbookList.map((workbook) => (
            <a href="#">{workbook.name} <br /></a>
          ))}
        </div>
      </div>
    </>
  ) 
}
