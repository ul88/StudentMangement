"use client"
import { useState, useEffect } from "react"
import { Route, Routes, Link, useLocation } from "react-router-dom"
import Student from "./student/page"
import Workbook from "./workbook/page"
import { Button } from "./components/ui/button"
import LoginForm from "./auth/page"
import WorkbookDetail from "./workbookDetails/page"
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu"

export default function App() {
  const location = useLocation()
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  useEffect(() => {
    // 로그인 상태 확인
    const token = localStorage.getItem("access_token")
    setIsLoggedIn(!!token)
  }, [location])

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b sticky top-0 z-50 w-full bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
        <div className="container flex h-16 items-center">
          <NavigationMenu>
            <NavigationMenuList>
              <NavigationMenuItem>
                <Link to="/">
                  <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                    <span className="font-bold">학생 관리 시스템</span>
                  </NavigationMenuLink>
                </Link>
              </NavigationMenuItem>
              <NavigationMenuItem>
                <Link to="/student">
                  <NavigationMenuLink className={navigationMenuTriggerStyle()}>학생 관리</NavigationMenuLink>
                </Link>
              </NavigationMenuItem>
              <NavigationMenuItem>
                <Link to="/workbook">
                  <NavigationMenuLink className={navigationMenuTriggerStyle()}>문제집 관리</NavigationMenuLink>
                </Link>
              </NavigationMenuItem>
            </NavigationMenuList>
          </NavigationMenu>
          <div className="ml-auto flex items-center space-x-4">
            {isLoggedIn ? (
              <Button
                variant="outline"
                onClick={() => {
                  localStorage.removeItem("refresh_token")
                  setIsLoggedIn(false)
                  window.location.href = "/login"
                }}
              >
                로그아웃
              </Button>
            ) : (
              <Link to="/login">
                <Button variant="default">로그인</Button>
              </Link>
            )}
          </div>
        </div>
      </header>

      <main className="container py-6">
        <Routes>
          <Route path="/login" element={<LoginForm />}></Route>
          <Route path="/student" element={<Student />}></Route>
          <Route path="/workbook" element={<Workbook />}></Route>
          <Route path="/workbook/:id" element={<WorkbookDetail />} />
          <Route path="/" element={<Home />} />
        </Routes>
      </main>
    </div>
  )
}

function Home() {
  return (
    <div className="flex flex-col items-center justify-center space-y-6 text-center py-12">
      <h1 className="text-4xl font-bold tracking-tight">학생 관리 시스템</h1>
      <p className="text-muted-foreground max-w-[600px]">
        학생들의 문제 풀이 현황을 관리하고 문제집을 생성하여 학습 진도를 효과적으로 관리하세요.
      </p>
      <div className="flex gap-4">
        <Link to="/student">
          <Button size="lg">학생 관리</Button>
        </Link>
        <Link to="/workbook">
          <Button size="lg" variant="outline">
            문제집 관리
          </Button>
        </Link>
      </div>
    </div>
  )
}

