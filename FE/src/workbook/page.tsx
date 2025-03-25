"use client"

import axiosConfig from "../config/axiosConfig"
import { useEffect, useState } from "react"
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import {z} from "zod"
import { Button } from "@/components/ui/button"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { useNavigate } from "react-router-dom"

const user = z.object({
    name : z
        .string()
        .min(3)
        .max(20),
    problemList : z
        .string()
})


export default function Workbook(){
    const [workbookList, setWorkbookList] = useState<any>([])
    const nav = useNavigate();
    const form = useForm<z.infer<typeof user>>({
                resolver: zodResolver(user),
                defaultValues:{
                    name : "",
                    problemList: ""
                },
            })

    useEffect(()=>{
        getWorkbooks()
    }, [])

    async function getWorkbooks(){
        const {data} = await axiosConfig.get("/api/workbook")
        setWorkbookList(data)
    }

    async function getWorkbook(id: any) {
        nav(`/workbook/${id}`);
      }

    async function addWorkbook(workbookInput : z.infer<typeof user>){
        try{
            const problems = workbookInput.problemList.split(",")

            const workbookDto = {
                name : workbookInput.name,
                problemList : problems
            }
            await axiosConfig.post("/api/workbook", workbookDto)
            setWorkbookList({...workbookList, workbookDto})
            nav("/")
        }catch(e){
          console.log(e)
        }
    }

    async function deleteWorkbook(id: string) {
        try {
          await axiosConfig.delete(`/api/workbook/${id}`);
          alert("문제집을 삭제했습니다.");
        } catch (e) {
          console.log(e);
        }
    }

    return (
        <>
            <div style={{
            border: "3px solid black",
            padding : "15px",
            marginTop : "10px",
            width: "30%",
            height: "10%",
            marginLeft: "5px"
        }}>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(addWorkbook)} className="space-y-3">
                    <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>이름</FormLabel>
                        <FormControl>
                            <Input placeholder="이름을 입력해주세요." {...field} />
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
                        <FormLabel>문제</FormLabel>
                        <FormControl>
                            <Input placeholder=",로 구분지어 문제를 입력해주세요" {...field} />
                        </FormControl>
                        <FormMessage />
                        </FormItem>
                    )}
                    />
                    <Button type="submit">추가하기</Button>
                </form>
            </Form>
        </div>
            <div>
                {workbookList.map((workbook : any) => (
                    <div key={workbook.id}>
                    <a href="" onClick={() => getWorkbook(workbook.id)}>{workbook.name}</a>
                    <Button onClick={() => deleteWorkbook(workbook.id)}>삭제</Button>
                    <br />
                  </div>
                ))}
            </div>
        </>
    )
}