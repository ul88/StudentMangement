"use client"
import { useState, useEffect } from "react";
import { Route, Routes, Link } from "react-router-dom";
import Student from "./student/page";
import Workbook from "./workbook/page";
import { Button, buttonVariants } from "./components/ui/button";
import LoginForm from "./auth/page";
import WorkbookDetail from "./workbookDetails/page";

export default function App(){
  return (
    <>
      <Link to="/" className={buttonVariants({})}>홈</Link> 
      <Link to="/login" className={buttonVariants({})}>로그인</Link>
      <Link to="/student" className={buttonVariants({})}>학생 보기</Link>
      <Link to="/workbook" className={buttonVariants({})}>문제집 보기</Link>
      <Routes>
          <Route path="/login" element={<LoginForm />}></Route>
          <Route path="/student" element={<Student />}></Route>
          <Route path="/workbook" element={<Workbook/>}></Route>
          <Route path="/workbook/:id" element={<WorkbookDetail />} />
      </Routes>
    </>
  ) 
}
