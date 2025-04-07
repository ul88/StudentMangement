"use client"

import { z } from "zod"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import axiosConfig from "../config/axiosConfig"
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { useEffect, useState, useRef } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { RefreshCw, Trash2 } from "lucide-react"
import { toast } from "sonner"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Progress } from "@/components/ui/progress"

const user = z.object({
  name: z
    .string()
    .min(1, { message: "이름은 필수 입력 항목입니다" })
    .max(20, { message: "이름은 최대 20자까지 가능합니다" }),
  bojId: z
    .string()
    .min(4, { message: "백준 아이디는 최소 4자 이상이어야 합니다" })
    .max(30, { message: "백준 아이디는 최대 30자까지 가능합니다" }),
  birth: z
    .string()
    .min(8, { message: "생년월일은 8자리로 입력해주세요 (YYYYMMDD)" })
    .max(8, { message: "생년월일은 8자리로 입력해주세요 (YYYYMMDD)" }),
})

export default function Student() {
  const [studentList, setStudentList] = useState<any>([])
  const [isLoading, setIsLoading] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [selectedStudent, setSelectedStudent] = useState<string | null>(null)
  const [renewingStudents, setRenewingStudents] = useState<Record<string, boolean>>({})
  const [isRenewingAll, setIsRenewingAll] = useState(false)
  const [renewProgress, setRenewProgress] = useState(0)
  const [currentRenewIndex, setCurrentRenewIndex] = useState(0)
  const timerRef = useRef<NodeJS.Timeout | null>(null)

  const form = useForm<z.infer<typeof user>>({
    resolver: zodResolver(user),
    defaultValues: {
      name: "",
      bojId: "",
      birth: "",
    },
  })

  useEffect(() => {
    getStudents()
  }, [])

  async function getStudents() {
    setIsLoading(true)
    try {
      const { data } = await axiosConfig.get("/api/student")
      setStudentList(data)
    } catch (e) {
      console.log(e)
      toast.error("학생 목록을 불러오는데 실패했습니다.")
    } finally {
      setIsLoading(false)
    }
  }

  async function addStudent(studentInput: z.infer<typeof user>) {
    setIsSubmitting(true)
    try {
      await axiosConfig.post("/api/student", studentInput)
      getStudents()
      form.reset()
      toast.success(`${studentInput.name} 학생이 추가되었습니다.`)
    } catch (e) {
      console.log(e)
      toast.error("학생 추가에 실패했습니다.")
    } finally {
      setIsSubmitting(false)
    }
  }

  async function renewStudent(id: string) {
    try {
      // 갱신 상태를 true로 설정
      setRenewingStudents((prev) => ({ ...prev, [id]: true }))

      await axiosConfig.post(`/api/management/${id}`)
      toast.success("학생이 푼 문제를 갱신했습니다.")
    } catch (e) {
      console.log(e)
      toast.error("학생 정보 갱신에 실패했습니다.")
    } finally {
      // 갱신 상태를 false로 설정
      setRenewingStudents((prev) => ({ ...prev, [id]: false }))
    }
  }

  function renewAllStudents() {
    if (studentList.length === 0) {
      toast.error("갱신할 학생이 없습니다.")
      return
    }

    // 이미 갱신 중이면 중복 실행 방지
    if (isRenewingAll) {
      return
    }

    setIsRenewingAll(true)
    setCurrentRenewIndex(0)
    setRenewProgress(0)

    // 현재 진행 중인 타이머가 있다면 정리
    if (timerRef.current) {
      clearInterval(timerRef.current)
    }

    // 첫 번째 학생 갱신 즉시 시작
    const firstStudent = studentList[0]
    renewStudent(firstStudent.id)

    let currentIndex = 1 // 첫 번째 학생은 이미 갱신 시작했으므로 1부터 시작
    setRenewProgress(Math.round((1 / studentList.length) * 100))

    // 5초마다 다음 학생 갱신
    timerRef.current = setInterval(() => {
      if (currentIndex >= studentList.length) {
        // 모든 학생 갱신 완료
        if (timerRef.current) {
          clearInterval(timerRef.current)
          timerRef.current = null
        }
        setIsRenewingAll(false)
        toast.success("모든 학생 정보 갱신이 완료되었습니다.")
        return
      }

      // 다음 학생 갱신
      const student = studentList[currentIndex]
      renewStudent(student.id)

      // 인덱스 및 진행률 업데이트
      setCurrentRenewIndex(currentIndex)
      setRenewProgress(Math.round(((currentIndex + 1) / studentList.length) * 100))

      // 다음 학생으로 이동
      currentIndex++
    }, 5000)
  }

  async function deleteStudent(id: string) {
    try {
      await axiosConfig.delete(`/api/student/${id}`)
      setStudentList(studentList.filter((student: any) => student.id !== id))
      toast.success("학생이 삭제되었습니다.")
    } catch (e) {
      console.log(e)
      toast.error("학생 삭제에 실패했습니다.")
    }
  }

  function formatBirthDate(birth: string) {
    if (birth.length !== 8) return birth
    return `${birth.substring(0, 4)}-${birth.substring(4, 6)}-${birth.substring(6, 8)}`
  }

  return (
    <Tabs defaultValue="list" className="w-full">
      <TabsList className="grid w-full max-w-md grid-cols-2 mb-6">
        <TabsTrigger value="list">학생 목록</TabsTrigger>
        <TabsTrigger value="add">학생 추가</TabsTrigger>
      </TabsList>

      <TabsContent value="list">
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>학생 목록</CardTitle>
                <CardDescription>등록된 학생 목록을 관리하고 문제 풀이 현황을 갱신할 수 있습니다.</CardDescription>
              </div>
              <Button variant="outline" onClick={renewAllStudents} disabled={isRenewingAll || isLoading}>
                {isRenewingAll ? (
                  <>
                    <RefreshCw className="h-4 w-4 mr-1 animate-spin" /> 전체 갱신 중...
                  </>
                ) : (
                  <>
                    <RefreshCw className="h-4 w-4 mr-1" /> 전체 갱신
                  </>
                )}
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            {isRenewingAll && (
              <div className="mb-4 space-y-2">
                <div className="flex justify-between text-sm">
                  <span>
                    갱신 진행 중: {currentRenewIndex + 1}/{studentList.length}
                  </span>
                  <span>{renewProgress}%</span>
                </div>
                <Progress value={renewProgress} className="h-2" />
                <p className="text-sm text-muted-foreground">
                  {studentList[currentRenewIndex]?.name} 학생 정보 갱신 중...
                </p>
              </div>
            )}

            {isLoading ? (
              <div className="flex justify-center py-8">
                <RefreshCw className="h-8 w-8 animate-spin text-muted-foreground" />
              </div>
            ) : (
              <Table>
                <TableCaption>등록된 학생 목록</TableCaption>
                <TableHeader>
                  <TableRow>
                    <TableHead>이름</TableHead>
                    <TableHead>백준 ID</TableHead>
                    <TableHead>생년월일</TableHead>
                    <TableHead className="text-right">관리</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {studentList.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="text-center py-8 text-muted-foreground">
                        등록된 학생이 없습니다.
                      </TableCell>
                    </TableRow>
                  ) : (
                    studentList.map((student: any) => (
                      <TableRow key={student.id}>
                        <TableCell className="font-medium">{student.name}</TableCell>
                        <TableCell>{student.bojId}</TableCell>
                        <TableCell>{formatBirthDate(student.birth)}</TableCell>
                        <TableCell className="text-right">
                          <div className="flex justify-end gap-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => renewStudent(student.id)}
                              disabled={renewingStudents[student.id] || isRenewingAll}
                            >
                              {renewingStudents[student.id] ? (
                                <>
                                  <RefreshCw className="h-4 w-4 mr-1 animate-spin" /> 갱신 중
                                </>
                              ) : (
                                <>
                                  <RefreshCw className="h-4 w-4 mr-1" /> 갱신
                                </>
                              )}
                            </Button>
                            <Dialog>
                              <DialogTrigger asChild>
                                <Button
                                  variant="destructive"
                                  size="sm"
                                  onClick={() => setSelectedStudent(student.id)}
                                  disabled={isRenewingAll}
                                >
                                  <Trash2 className="h-4 w-4 mr-1" /> 삭제
                                </Button>
                              </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>학생 삭제</DialogTitle>
                                  <DialogDescription>
                                    정말로 {studentList.find((s: any) => s.id === selectedStudent)?.name} 학생을
                                    삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.
                                  </DialogDescription>
                                </DialogHeader>
                                <DialogFooter>
                                  <Button variant="outline" onClick={() => setSelectedStudent(null)}>
                                    취소
                                  </Button>
                                  <Button
                                    variant="destructive"
                                    onClick={() => {
                                      if (selectedStudent) {
                                        deleteStudent(selectedStudent)
                                        setSelectedStudent(null)
                                      }
                                    }}
                                  >
                                    삭제
                                  </Button>
                                </DialogFooter>
                              </DialogContent>
                            </Dialog>
                          </div>
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>
      </TabsContent>

      <TabsContent value="add">
        <Card>
          <CardHeader>
            <CardTitle>학생 추가</CardTitle>
            <CardDescription>새로운 학생 정보를 입력하여 등록하세요.</CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form onSubmit={form.handleSubmit(addStudent)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>이름</FormLabel>
                      <FormControl>
                        <Input placeholder="이름을 입력해주세요" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="bojId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>백준 아이디</FormLabel>
                      <FormControl>
                        <Input placeholder="백준 아이디를 입력해주세요" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="birth"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>생년월일</FormLabel>
                      <FormControl>
                        <Input placeholder="YYYYMMDD 형식으로 입력해주세요" {...field} />
                      </FormControl>
                      <FormDescription>예: 20050101 (2005년 1월 1일)</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <Button type="submit" className="w-full" disabled={isSubmitting}>
                  {isSubmitting ? "추가 중..." : "학생 추가"}
                </Button>
              </form>
            </Form>
          </CardContent>
        </Card>
      </TabsContent>
    </Tabs>
  )
}