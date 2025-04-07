"use client"

import axiosConfig from "../config/axiosConfig"
import { useEffect, useState } from "react"
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { z } from "zod"
import { Button } from "@/components/ui/button"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Book, RefreshCw, Trash2 } from "lucide-react"
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
import { Textarea } from "@/components/ui/textarea"

const workbookSchema = z.object({
  name: z
    .string()
    .min(1, { message: "문제집 이름은 필수 입력 항목입니다" })
    .max(50, { message: "문제집 이름은 최대 50자까지 가능합니다" }),
  problemList: z.string().min(1, { message: "문제 목록은 필수 입력 항목입니다" }),
})

export default function Workbook() {
  const [workbookList, setWorkbookList] = useState<any>([])
  const [isLoading, setIsLoading] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [selectedWorkbook, setSelectedWorkbook] = useState<string | null>(null)
  const nav = useNavigate()

  const form = useForm<z.infer<typeof workbookSchema>>({
    resolver: zodResolver(workbookSchema),
    defaultValues: {
      name: "",
      problemList: "",
    },
  })

  useEffect(() => {
    getWorkbooks()
  }, [])

  async function getWorkbooks() {
    setIsLoading(true)
    try {
      const { data } = await axiosConfig.get("/api/workbook")
      setWorkbookList(data)
    } catch (error) {
      console.error(error)
      toast.error("문제집 목록을 불러오는데 실패했습니다.")
    } finally {
      setIsLoading(false)
    }
  }

  async function getWorkbook(id: string) {
    nav(`/workbook/${id}`)
  }

  async function addWorkbook(workbookInput: z.infer<typeof workbookSchema>) {
    setIsSubmitting(true)
    try {
      const problems = workbookInput.problemList
        .split(",")
        .map((p) => p.trim())
        .filter((p) => p)

      const workbookDto = {
        name: workbookInput.name,
        problemList: problems,
      }

      await axiosConfig.post("/api/workbook", workbookDto)
      getWorkbooks()
      form.reset()

      toast.success(`${workbookInput.name} 문제집이 추가되었습니다.`)
    } catch (e) {
      console.log(e)
      toast.error("문제집 추가에 실패했습니다.")
    } finally {
      setIsSubmitting(false)
    }
  }

  async function deleteWorkbook(id: string) {
    try {
      await axiosConfig.delete(`/api/workbook/${id}`)
      setWorkbookList(workbookList.filter((workbook: any) => workbook.id !== id))
      toast.success("문제집이 삭제되었습니다.")
    } catch (e) {
      console.log(e)
      toast.error("문제집 삭제에 실패했습니다.")
    }
  }

  return (
    <Tabs defaultValue="list" className="w-full">
      <TabsList className="grid w-full max-w-md grid-cols-2 mb-6">
        <TabsTrigger value="list">문제집 목록</TabsTrigger>
        <TabsTrigger value="add">문제집 추가</TabsTrigger>
      </TabsList>

      <TabsContent value="list">
        <Card>
          <CardHeader>
            <CardTitle>문제집 목록</CardTitle>
            <CardDescription>등록된 문제집 목록을 관리하고 상세 내용을 확인할 수 있습니다.</CardDescription>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <div className="flex justify-center py-8">
                <RefreshCw className="h-8 w-8 animate-spin text-muted-foreground" />
              </div>
            ) : (
              <Table>
                <TableCaption>등록된 문제집 목록</TableCaption>
                <TableHeader>
                  <TableRow>
                    <TableHead>문제집 이름</TableHead>
                    <TableHead className="text-right">관리</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {workbookList.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={2} className="text-center py-8 text-muted-foreground">
                        등록된 문제집이 없습니다.
                      </TableCell>
                    </TableRow>
                  ) : (
                    workbookList.map((workbook: any) => (
                      <TableRow key={workbook.id}>
                        <TableCell className="font-medium">
                          <Button
                            variant="link"
                            className="p-0 h-auto font-medium text-left"
                            onClick={() => getWorkbook(workbook.id)}
                          >
                            {workbook.name}
                          </Button>
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex justify-end gap-2">
                            <Button variant="outline" size="sm" onClick={() => getWorkbook(workbook.id)}>
                              <Book className="h-4 w-4 mr-1" /> 상세보기
                            </Button>
                            <Dialog>
                              <DialogTrigger asChild>
                                <Button
                                  variant="destructive"
                                  size="sm"
                                  onClick={() => setSelectedWorkbook(workbook.id)}
                                >
                                  <Trash2 className="h-4 w-4 mr-1" /> 삭제
                                </Button>
                              </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>문제집 삭제</DialogTitle>
                                  <DialogDescription>
                                    정말로 {workbookList.find((w: any) => w.id === selectedWorkbook)?.name} 문제집을
                                    삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.
                                  </DialogDescription>
                                </DialogHeader>
                                <DialogFooter>
                                  <Button variant="outline" onClick={() => setSelectedWorkbook(null)}>
                                    취소
                                  </Button>
                                  <Button
                                    variant="destructive"
                                    onClick={() => {
                                      if (selectedWorkbook) {
                                        deleteWorkbook(selectedWorkbook)
                                        setSelectedWorkbook(null)
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
            <CardTitle>문제집 추가</CardTitle>
            <CardDescription>새로운 문제집 정보를 입력하여 등록하세요.</CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form onSubmit={form.handleSubmit(addWorkbook)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>문제집 이름</FormLabel>
                      <FormControl>
                        <Input placeholder="문제집 이름을 입력해주세요" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="problemList"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>문제 목록</FormLabel>
                      <FormControl>
                        <Textarea
                          placeholder="문제 ID를 쉼표(,)로 구분하여 입력해주세요"
                          className="min-h-[100px]"
                          {...field}
                        />
                      </FormControl>
                      <FormDescription>예: 1000, 1001, 1002</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <Button type="submit" className="w-full" disabled={isSubmitting}>
                  {isSubmitting ? "추가 중..." : "문제집 추가"}
                </Button>
              </form>
            </Form>
          </CardContent>
        </Card>
      </TabsContent>
    </Tabs>
  )
}

