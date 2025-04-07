"use client"

import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axiosConfig from "../config/axiosConfig"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { ArrowLeft, Check, RefreshCw, X } from "lucide-react"
import { toast } from "sonner"
import { Skeleton } from "@/components/ui/skeleton"

interface Student {
  id: string
  name: string
  bojId: string
}

interface Problem {
  id: string
  name: string
  level: string
}

interface Workbook {
  id: string
  name: string
  problems: Problem[]
}

interface SolvedStatus {
  [problemId: string]: {
    [studentId: string]: boolean
  }
}

export default function WorkbookDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [isLoading, setIsLoading] = useState(true)
  const [workbook, setWorkbook] = useState<Workbook>({
    id: "",
    name: "",
    problems: [],
  })

  const [students, setStudents] = useState<Student[]>([])
  const [solvedStatus, setSolvedStatus] = useState<SolvedStatus>({})

  useEffect(() => {
    Promise.all([getWorkbook(), getStudents(), getSolvedStatus()])
      .then(() => {
        setIsLoading(false)
      })
      .catch(() => {
        setIsLoading(false)
      })
  }, [id])

  async function getWorkbook() {
    try {
      const { data } = await axiosConfig.get(`/api/workbook/${id}`)
      setWorkbook(data)
      return data
    } catch (error) {
      console.error("문제집을 불러오는 데 실패했습니다.", error)
      toast.error("문제집 정보를 불러오는데 실패했습니다.")
      throw error
    }
  }

  async function getStudents() {
    try {
      const { data } = await axiosConfig.get(`/api/student`)
      setStudents(data)
      return data
    } catch (error) {
      console.error("학생 목록을 불러오는 데 실패했습니다.", error)
      toast.error("학생 목록을 불러오는데 실패했습니다.")
      throw error
    }
  }

  async function getSolvedStatus() {
    try {
      const { data } = await axiosConfig.get(`/api/management?workbookId=${id}`)

      // 문제별 학생 풀이 상태를 매핑
      const statusMap: SolvedStatus = {}

      data.forEach((item: any) => {
        const problemId = item.problem.id
        statusMap[problemId] = {}

        // 모든 학생에 대해 기본값으로 false 설정
        students.forEach((student) => {
          statusMap[problemId][student.id] = false
        })

        // 문제를 푼 학생들은 true로 설정
        item.studentList.forEach((student: any) => {
          statusMap[problemId][student.id] = true
        })
      })

      setSolvedStatus(statusMap)
      return statusMap
    } catch (error) {
      console.error("풀이 현황을 불러오는 데 실패했습니다.", error)
      toast.error("풀이 현황을 불러오는데 실패했습니다.")
      throw error
    }
  }

  function refreshData() {
    setIsLoading(true)
    Promise.all([getWorkbook(), getStudents(), getSolvedStatus()])
      .then(() => {
        setIsLoading(false)
        toast.success("문제집 정보를 새로고침했습니다.")
      })
      .catch(() => {
        setIsLoading(false)
      })
  }

  function getLevelColor(level: string) {
    if (level.includes("Bronze")) return "bg-yellow-700"
    if (level.includes("Silver")) return "bg-gray-500"
    if (level.includes("Gold")) return "bg-yellow-500"
    if (level.includes("Platinum")) return "bg-emerald-400"
    if (level.includes("Diamond")) return "bg-sky-500"
    if (level.includes("Ruby")) return "bg-red-400"
    return "bg-lime-300"
  }

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <Skeleton className="h-8 w-3/4" />
          <Skeleton className="h-4 w-1/2" />
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <Skeleton className="h-6 w-1/4" />
            <div className="space-y-2">
              {[1, 2, 3, 4, 5].map((i) => (
                <Skeleton key={i} className="h-12 w-full" />
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div>
            <CardTitle className="text-2xl">{workbook.name}</CardTitle>
            <CardDescription>
              문제 수: {workbook.problems?.length || 0}개 / 학생 수: {students.length}명
            </CardDescription>
          </div>
          <Button variant="outline" size="sm" onClick={() => navigate(-1)}>
            <ArrowLeft className="h-4 w-4 mr-1" /> 뒤로 가기
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        <h2 className="text-xl font-semibold mb-4">문제 풀이 현황</h2>
        <div className="overflow-x-auto">
          <Table>
            <TableCaption>문제집 풀이 현황 (O: 풀었음, X: 풀지 않음)</TableCaption>
            <TableHeader>
              <TableRow>
                <TableHead className="w-[80px]">레벨</TableHead>
                <TableHead className="w-[100px]">문제 ID</TableHead>
                <TableHead className="min-w-[200px]">문제 이름</TableHead>
                {students.map((student) => (
                  <TableHead key={student.id} className="text-center">
                    {student.name}
                  </TableHead>
                ))}
              </TableRow>
            </TableHeader>
            <TableBody>
              {workbook.problems && workbook.problems.length > 0 ? (
                workbook.problems.map((problem) => (
                  <TableRow key={problem.id}>
                    <TableCell>
                      <Badge className={`${getLevelColor(problem.level)}`}>{problem.level}</Badge>
                    </TableCell>
                    <TableCell onClick={() => window.open("https://boj.kr/"+problem.id,'_blank')}>{problem.id}</TableCell>
                    <TableCell onClick={() => window.open("https://boj.kr/"+problem.id,'_blank')} className="font-medium">{problem.name}</TableCell>
                    {students.map((student) => (
                      <TableCell key={`${problem.id}-${student.id}`} className="text-center">
                        {solvedStatus[problem.id] && solvedStatus[problem.id][student.id] ? (
                          <div className="flex justify-center">
                            <Check className="h-5 w-5 text-green-600" />
                          </div>
                        ) : (
                          <div className="flex justify-center">
                            <X className="h-5 w-5 text-red-600" />
                          </div>
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={3 + students.length} className="text-center py-8 text-muted-foreground">
                    등록된 문제가 없습니다.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
      </CardContent>
      <CardFooter className="flex justify-end">
        <Button variant="outline" onClick={refreshData}>
          <RefreshCw className="h-4 w-4 mr-1" /> 새로고침
        </Button>
      </CardFooter>
    </Card>
  )
}

