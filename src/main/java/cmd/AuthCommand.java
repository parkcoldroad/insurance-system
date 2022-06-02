package cmd;

import cmd.menu.AuthMenu;
import cmd.parser.AuthParser;
import cmd.parser.Parser;
import dto.AuthDto;
import java.util.Arrays;
import service.impl.AuthServiceImpl;

public class AuthCommand {

  private static final AuthParser parser = AuthParser.getInstance();
  private static final AuthServiceImpl authService = new AuthServiceImpl();

  public static void run() {
    System.out.println("[Auth]");

    Arrays.stream(AuthMenu.values()).forEach(menu -> {
      System.out.println(
          Integer.toString(menu.ordinal() + 1)
              .concat(". ")
              .concat(menu.toString()));
    });

    System.out.print("> ");
    int selectedMenu = Parser.getScanner().nextInt();

    Arrays.stream(AuthMenu.values()).forEach(menu -> {
      if (selectedMenu == menu.ordinal() + 1) {
        menu.execute();
      }
    });
  }

  public static void signin() {
    System.out.println("[Sign In]");
    AuthDto.SigninRequest request = AuthDto.SigninRequest.builder()
        .email(parser.getEmail())
        .password(parser.getPassword())
        .build();
    authService.signin(request);
  }

  public static void signup() {
    System.out.println("[Sign Up]");
    AuthDto.SignupRequest request = AuthDto.SignupRequest.builder()
        .email(parser.getEmail())
        .password(parser.getPassword())
        .name(parser.getName())
        .employeeType(parser.getEmployeeType())
        .build();
    authService.signup(request);
  }
}
