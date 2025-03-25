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
import { useNavigate } from "react-router-dom"

const user = z.object({
    id : z
        .string()
        .min(5)
        .max(20),
    password : z
        .string()
        .min(4)
        .max(30),
})

export default function LoginForm(){
    const navigate = useNavigate();
    const form = useForm<z.infer<typeof user>>({
        resolver: zodResolver(user),
        defaultValues:{
            id : "",
            password : "",
        },
    })

    async function onSubmit(loginInput: z.infer<typeof user>){
        try{
          await axiosConfig.post("/api/login",loginInput, {withCredentials : true})
          navigate("/");
        }catch(e){
          console.log(e)
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
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-3">
            <FormField
              control={form.control}
              name="id"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>아이디</FormLabel>
                  <FormControl>
                    <Input placeholder="input id" {...field} />
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
                    <Input placeholder="input password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit">Login</Button>
        </form>
      </Form>
      </div>
      </>
    )
}