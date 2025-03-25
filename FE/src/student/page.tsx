"use client"

import {z} from "zod"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import axiosConfig from "../config/axiosConfig"
import { Button } from "@/components/ui/button"
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
import {useEffect ,useState } from "react"

const user = z.object({
    name : z
        .string()
        .min(1)
        .max(20),
    bojId : z
        .string()
        .min(4)
        .max(30),
    birth : z
        .string()
        .min(8)
        .max(8)
})

export default function Student(){
    const [studentList, setStudentList] = useState<any>([])
    const form = useForm<z.infer<typeof user>>({
            resolver: zodResolver(user),
            defaultValues:{
                name : "",
                bojId : "",
                birth: ""
            },
        })


    useEffect(() =>{
        getStudents()
    }, [])
    
    async function getStudents(){
        const {data} = await axiosConfig.get("/api/student")
        setStudentList(data)
    }

    async function addStudent(studentInput : z.infer<typeof user>){
        try{
          const {data} = await axiosConfig.post("/api/student", studentInput)
          setStudentList([...studentList, data])
        }catch(e){
          console.log(e)
        }
    }

    async function renewStudent(id: string) {
        try {
          await axiosConfig.post(`/api/management/${id}`);
          alert("학생이 푼 문제를 갱신했습니다.");
        } catch (e) {
          console.log(e);
        }
    }

    async function deleteStudent(id: string) {
        try {
          await axiosConfig.delete(`/api/student/${id}`);
          alert("학생을 삭제했습니다.");
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
                <form onSubmit={form.handleSubmit(addStudent)} className="space-y-3">
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
                    name="bojId"
                    render={({ field }) => (
                        <FormItem>
                        <FormLabel>백준 아이디</FormLabel>
                        <FormControl>
                            <Input placeholder="백준 아이디를 입력해주세요." {...field} />
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
                        <FormLabel>생일</FormLabel>
                        <FormControl>
                            <Input placeholder="생일을 입력해주세요." {...field} />
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
                {studentList.map((student : any) =>(
                    <div key={student.id} className="flex items-center gap-4 mt-2">
                    <a href="#">{student.name}</a>
                    <Button onClick={() => renewStudent(student.id)}>갱신</Button>
                    <Button onClick={() => deleteStudent(student.id)}>삭제</Button>
                  </div>
                ))}
            </div>

        </>
    )
}