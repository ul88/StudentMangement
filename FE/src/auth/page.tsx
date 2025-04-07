"use client"

import { z } from "zod"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import axiosConfig from "../config/axiosConfig"
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useState } from "react"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { AlertCircle } from "lucide-react"
import { toast } from "sonner"

const user = z.object({
  id: z
    .string()
    .min(5, { message: "아이디는 최소 5자 이상이어야 합니다" })
    .max(20, { message: "아이디는 최대 20자까지 가능합니다" }),
  password: z
    .string()
    .min(4, { message: "비밀번호는 최소 4자 이상이어야 합니다" })
    .max(30, { message: "비밀번호는 최대 30자까지 가능합니다" }),
})

export default function LoginForm() {
  const navigate = useNavigate()
  const [error, setError] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const form = useForm<z.infer<typeof user>>({
    resolver: zodResolver(user),
    defaultValues: {
      id: "",
      password: "",
    },
  })

  async function onSubmit(loginInput: z.infer<typeof user>) {
    try {
      setIsLoading(true)
      setError("")
      await axiosConfig.post("/api/login", loginInput, { withCredentials: true })
      toast.success("로그인에 성공했습니다.")
      navigate("/")
    } catch (e: any) {
      console.log(e)
      setError(e.response?.data?.message || "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.")
      toast.error("로그인에 실패했습니다.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="flex justify-center items-center min-h-[80vh]">
      <Card className="w-full max-w-md">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold">로그인</CardTitle>
          <CardDescription>아이디와 비밀번호를 입력하여 로그인하세요</CardDescription>
        </CardHeader>
        <CardContent>
          {error && (
            <Alert variant="destructive" className="mb-4">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>오류</AlertTitle>
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="id"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>아이디</FormLabel>
                    <FormControl>
                      <Input placeholder="아이디를 입력하세요" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>비밀번호</FormLabel>
                    <FormControl>
                      <Input type="password" placeholder="비밀번호를 입력하세요" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <Button type="submit" className="w-full" disabled={isLoading}>
                {isLoading ? "로그인 중..." : "로그인"}
              </Button>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  )
}

