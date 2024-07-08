package org.example;
import java.util.*;
import java.util.Date;

class Main{
    public static void main(String[] args){
        System.out.println("Library port initialized..");
        int command;
        library mylibrary=new library();
        librarian owner = new librarian(mylibrary);
        Scanner my_scanner= new Scanner(System.in);
        while(true){
            menu_interface();
            command=my_scanner.nextInt();
            my_scanner.nextLine();
            if(command==1){
                librarian.login();
            }
            else if(command==2){
                String name;
                int number;
                System.out.print("Enter name: ");
                name=my_scanner.nextLine();
                System.out.print("Enter MemberID number: ");
                number=my_scanner.nextInt();
                my_scanner.nextLine();
                int valid=owner.check_user(number,name);
                if(valid>=0){
                    System.out.printf("Hello %s,what would you like to do?\n",name);
                    librarian.getuserlogin(valid);
                }
                else{
                    System.out.printf("Member with name: %s and Phone no. %d doesn't exist!!\n",name,number);
                }
            }
            else if(command==3){
                System.out.println("Thank you for visiting!");
                my_scanner.close();
                System.exit(0);
            }
            else{
                System.out.println("Invalid input provided");
            }
        }
    }

    public static void menu_interface(){
        System.out.println("-----------------------------");
        System.out.println("1.    Enter as librarian");
        System.out.println("2.    Enter as member");
        System.out.println("3.    Exit");
        System.out.println("-----------------------------");
    }
}
class member{
    String name;
    int age;
    long Phone;
    int book_issued;
    library visiting_library;
    book book1;
    book book2;
    double fine;
    int memberID;
    public member(String name,int age,long phone_number,library lib,int id){
        this.name=name;
        this.age=age;
        this.Phone=phone_number;
        this.book1=null;
        this.book2=null;
        visiting_library=lib;
        this.fine=0;
        this.memberID=id;
    }
    public void list_mybooks(){
        switch(book_issued){
            case 0:
                System.out.println("No book issued currently");
                break;
            case 1:
                System.out.printf("%s\n",book1.getname());
                break;
            case 2:
                System.out.printf("%s\n%s\n",book1.getname(),book2.getname());
                break;
        }
    }
    public void list_available_book(){
        visiting_library.get_books();
    }
    private static void member_interface(){
        System.out.println("---------------------------------------");
        System.out.println("1. List available books");
        System.out.println("2. List my books");
        System.out.println("3. Issue book");
        System.out.println("4. Return book");
        System.out.println("5. Pay fine");
        System.out.println("6. Back");
        System.out.println("---------------------------------------");
    }
    public void login(){
        Scanner myscanner = new Scanner(System.in);
        int command;
        while(true){
            member_interface();
            command = myscanner.nextInt();
            myscanner.nextLine();
            if(command==1){
                visiting_library.get_books();
            }
            else if(command==2){
                list_mybooks();
            }
            else if(command==3){
                if(this.book_issued==2){
                    System.out.println("You cannot issue more than 2 books at a time");
                }
                else{
                    System.out.print("Enter the name of book: ");
                    String book=myscanner.nextLine();
                    book status = visiting_library.issue_book(book);
                    if(status==null){
                        System.out.println("Book with that name doesn't exist or isn't available");
                    }
                    else{
                        if(book_issued==0){
                            book1=status;
                        }
                        else{
                            book2=status;
                        }
                        book_issued++;
                        System.out.printf("Book with name %s has been issued to you, BOOKID: %d\n",book,status.bookID);
                    }
                }
            }
            else if(command==4){
                if(book_issued==0){
                    System.out.println("You have not issued any book");
                }
                else{
                    System.out.print("Book ID: ");
                    int id = myscanner.nextInt();
                    myscanner.nextLine();
                    if(book_issued==1){
                        if(book1.getIndex()==id){
                            this.fine+=book1.return_book(id);
                            book1=null;
                            book_issued--;
                        }
                        else{
                            System.out.println("Invalid ID bookID");
                        }
                    }
                    else{
                        if(book1.getIndex()==id){
                            this.fine+=book1.return_book(id);
                            book1=book2;
                            book2=null;
                            book_issued--;
                        }
                        if(book2.getIndex()==id){
                            this.fine+=book2.return_book(id);
                            book2=null;
                            book_issued--;
                        }
                        else{
                            System.out.println("Invalid ID bookID");
                        }
                    }
                }
            }
            else if(command==5){
                if(fine==0){
                    System.out.printf("you have no fines\n");
                }
                else{
                    System.out.printf("Your fine of %.2f has been paid, Thanks!\n",fine);
                    this.fine=0;
                }
            }
            else if(command==6){
                return;
            }
            else{
                System.out.println("Invalid number provided");
            }
        }

    }
    public long getphone(){
        return this.Phone;
    }
    public String getname(){
        return this.name;
    }
}
class librarian{
    private static library my_library;
    public librarian(library test_library){
        this.my_library=test_library;
    }
    public static void login(){
        Scanner my_scanner=new Scanner(System.in);
        int command;
        while(true){
            librarian_interface();
            command=my_scanner.nextInt();
            my_scanner.nextLine();
            if(command==1){
                String name;
                System.out.print("Enter member name: ");
                name = my_scanner.nextLine();
                System.out.print("Enter member age: ");
                int age=my_scanner.nextInt();
                my_scanner.nextLine();
                System.out.print("Enter member Phone no: ");
                long phone=my_scanner.nextLong();
                my_scanner.nextLine();
                int res = checkuserphone(name,phone);
                if(res<0){
                    my_library.add_member(name,age,phone,my_library);
                    System.out.printf("User has been successfully added with MEMBER-ID: %d\n",my_library.mem_present()-1);
                }
                else{
                    System.out.println("User with that name and number already exists");
                }
            }
            else if(command==2){
                System.out.print("Enter name of member: ");
                String member_name=my_scanner.nextLine();
                int number;
                System.out.print("Enter member-ID to delete: ");
                number = my_scanner.nextInt();
                my_scanner.nextLine();
                int index = check_user(number,member_name);
                if(index>=0){
                    if(my_library.send_user(index).book_issued>0){
                        System.out.println("User cannot be removed as he has books issued.");
                    }
                    else{
                        my_library.remove_member(number,index);
                        System.out.println("User removed");
                    }
                }
                else{
                    System.out.println("Error user doesn't exist");

                }
            }
            else if(command==3){
                System.out.print("Book to be added: ");
                String book_name=my_scanner.nextLine();
                System.out.print("Name of author: ");
                String author=my_scanner.nextLine();
                System.out.print("Number of copies: ");
                int number=my_scanner.nextInt();
                my_scanner.nextLine();
                for(int i=0;i<number;i++){
                    int id=my_library.add_book(book_name,author);
                    System.out.printf("%s has been added with ID- %d.\n",book_name,id-1);
                }
            }
            else if(command==4){
                System.out.print("Name of book to delete: ");
                String name=my_scanner.nextLine();
                int valid=my_library.check_book(name);
                if(valid>=0){
                    my_library.remove_book(name,valid);
                }
                else{
                    System.out.println("Error book that name isn't available");
                }

            }
            else if(command==5){
                my_library.show_members();
            }
            else if(command==6){
                my_library.get_books();
            }
            else if(command==7){
                return;
            }
            else{
                System.out.println("Invalid input provided");
            }
        }
    }

    private static void librarian_interface(){
        System.out.println("------------------------------------------------------------------");
        System.out.println("1. Register a member.");
        System.out.println("2. Remove a member.");
        System.out.println("3. Add a book.");
        System.out.println("4. Remove a book.");
        System.out.println("5. View all members along with their books and fines to be paid.");
        System.out.println("6. View all books");
        System.out.println("7. Back");
        System.out.println("-------------------------------------------------------------------");
    }
    public static int check_user(int number,String name){
        for(int i=0;i<my_library.mem_size();i++){
            if(my_library.send_user(i).memberID==number&&my_library.send_user(i).name.equalsIgnoreCase(name)==true){
                return i;
            }
        }
        return -1;
    }
    public static int checkuserphone(String name,long number){
        for(int i=0;i<my_library.mem_size();i++){
            if(my_library.send_user(i).Phone==number&&my_library.send_user(i).name.equalsIgnoreCase(name)==true){
                return i;
            }
        }
        return -1;
    }
    public static void getuserlogin(int index){
        my_library.send_user(index).login();
    }
}

class library{
    private book[] book_list;
    private int number_books;
    private int number_people;
    private member[] member_list;
    private int book_alloted;
    private int member_allotted;
    public library(){
        this.book_list=new book[100];
        this.member_list= new member[100];
        this.number_books=0;
        this.number_people=0;
        this.book_alloted=0;
        this.member_allotted=0;
    }
    public int add_member(String mem_name,int age,long phone,library lib){
        if(number_people>=100){
            return 1;
        }
        int id=this.member_allotted;
        member name=new member(mem_name,age,phone,lib,id);
        this.member_list[number_people]=name;
        number_people++;
        member_allotted++;
        return 0;
    }
    public int remove_member(long phone,int index){
        for(int i=index;i<mem_size()-1;i++){
            member_list[i]=member_list[i+1];
        }
        number_people--;
        return 0;
    }
    public int add_book(String name,String author){
        if(number_books>=100){
            return 1;
        }
        book name_book=new book(name,author,this.book_alloted);
        this.book_list[number_books]=name_book;
        number_books++;
        book_alloted++;
        return book_alloted;
    }
    public int remove_book(String name,int index){
        for(int i=index;i<number_books-1;i++){
            book_list[i]=book_list[i+1];
        }
        number_books--;
        return 0;
    }
    public void show_members(){
        for(int i=0;i<number_people;i++){
            System.out.printf("name: %s  ",member_list[i].name);
            if(member_list[i].book_issued==0){
                System.out.printf("No book has been issued  ");
            }
            if(member_list[i].book_issued==1){
                System.out.printf("Book1 = %s  ",member_list[i].book1.getname());
            }
            if(member_list[i].book_issued==2){
                System.out.printf("Book1 = %s  Book2 = %s  ",member_list[i].book1.getname(),member_list[i].book2.getname());
            }
            System.out.printf("Total fine is: %.2f\n",member_list[i].fine);
        }
    }
    public int mem_size(){
        return number_people;
    }
    public int book_size(){
        return number_books;
    }
    public member send_user(int index){
        return member_list[index];
    }
    public void get_books(){
        for(int i=0;i<number_books;i++){
            if(book_list[i].is_available()==true){
                System.out.printf("Book name: %s   Book ID: %d\n",book_list[i].getname(),book_list[i].getIndex());
            }
        }
    }
    public int check_book(String name){
        for(int i=0;i<number_books;i++){
            if(book_list[i].getname().equalsIgnoreCase(name)==true&&book_list[i].is_available()==true){
                return i;
            }
        }
        return -1;
    }
    public book issue_book(String name){
        int i=check_book(name);
        if(i>=0){
            book_list[i].issue_book();
            return book_list[i];
        }
        else{
            return null;
        }
    }
    public int mem_present(){
        return member_allotted;
    }
}

class book{
    String name;
    boolean available;
    int bookID;
    String author;
    private Date issue_time;
    public book(String name,String author,int bookID){
        this.name=name;
        this.available=true;
        this.bookID=bookID;
        this.issue_time=null;
        this.author=author;
    }
    public String getname(){
        return name;
    }
    public boolean is_available(){
        return available;
    }
    public int getIndex(){
        return bookID;
    }
    public void issue_book(){
        available=false;
        issue_time = new Date();
    }
    public double return_book(int id){
        double fine = ((new Date()).getTime() - issue_time.getTime())/1000;
        issue_time=null;
        available=true;
        System.out.printf("Book with ID-%d has been returned. your fine is %.2f for a delay of %.2f second\n",id,fine*3,fine);
        return fine*3;
    }
}