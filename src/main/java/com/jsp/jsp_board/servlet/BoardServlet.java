package com.jsp.jsp_board.servlet;

import com.jsp.jsp_board.DTO.BoardDTO;
import com.jsp.jsp_board.DTO.TestDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "boardServlet", value = "/board-servlet")
public class BoardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "jdbc:mysql://database-tong.cnasam86gevz.ap-northeast-2.rds.amazonaws.com:3306/tong";
        String user = "admin";
        String password = "15689725";

        // MySQL JDBC 드라이버 로드
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }

        Connection connection = null;
        List<BoardDTO> boardList = new ArrayList<>();

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database board");

            // 'board' 테이블 데이터 조회
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM board");

            // 결과 데이터를 Board 객체 리스트로 변환
            while (resultSet.next()) {
                BoardDTO board = new BoardDTO();
                board.setBoardId(resultSet.getInt("board_id"));
                board.setTitle(resultSet.getString("title"));;
                board.setContent(resultSet.getString("content"));
                board.setCategory(resultSet.getString("category"));
                board.setSubCategory(resultSet.getString("sub_category"));
                board.setHits(resultSet.getInt("hits"));
                board.setRecommend(resultSet.getInt("recommend"));
                board.setCreateDate(resultSet.getDate("create_date"));
                boardList.add(board);
            }

            // 데이터를 request에 설정하여 JSP 페이지로 전달
            request.setAttribute("boardData", boardList);
            request.getRequestDispatcher("component/main/board/index.jsp").forward(request, response);

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        } finally {
            // 연결 종료
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}