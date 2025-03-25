"use client";

import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosConfig from "../config/axiosConfig";
import { Button } from "@/components/ui/button";

export default function WorkbookDetail() {
  const { id } = useParams();
  const [workbook, setWorkbook] = useState<any>({
    id: "",
    name: "",
    problems: [{
        id: "",
        name: "",
        level: ""
    }]
  });

  const [solvedStudents, setSolvedStudents] = useState<any>({});

  useEffect(() => {
    getWorkbook();
    getSolvedStudents();
  }, []);

  async function getWorkbook() {
    try {
      const { data } = await axiosConfig.get(`/api/workbook/${id}`);
      setWorkbook(data);
    } catch (error) {
      console.error("문제집을 불러오는 데 실패했습니다.", error);
    }
  }

  async function getSolvedStudents() {
    try {
      const { data } = await axiosConfig.get(`/api/management?workbookId=${id}`);
      const studentMap = data.reduce((acc: any, item: any) => {
        acc[item.problem.id] = item.studentList.map((student: any) => student.name).join(", ");
        return acc;
      }, {});
      setSolvedStudents(studentMap);
    } catch (error) {
      console.error("푼 학생 정보를 불러오는 데 실패했습니다.", error);
    }
  }

  if (!workbook) {
    return <div>로딩 중...</div>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>{workbook.name}</h1>
      <h2>문제 목록</h2>
      <ul>
      {workbook.problems && workbook.problems.length > 0 ? (
          workbook.problems.map((problem: any) => (
            <li key={problem.id}>{problem.level} {problem.id} {problem.name} &nbsp;
            {solvedStudents[problem.id]}</li>
          ))
        ) : (
          <li>등록된 문제가 없습니다.</li>
        )}
      </ul>
      <Button onClick={() => window.history.back()}>뒤로 가기</Button>
    </div>
  );
}
