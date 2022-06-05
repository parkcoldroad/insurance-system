package command;

import command.menu.InsuranceDesignMenu;
import command.parser.InsuranceParser;
import domain.Insurance;
import domain.enums.AuthorizeType;
import domain.enums.InsuranceType;
import java.util.List;
import service.impl.InsuranceDesignServiceImpl;

public class InsuranceDesignCommand extends Command {

  private static final InsuranceParser insuranceParser = InsuranceParser.getInstance();
  private static final InsuranceDesignServiceImpl insuranceImpl = new InsuranceDesignServiceImpl();

  public static void run() {
    executeCommand("보험개발", InsuranceDesignMenu.values());
  }

  public static void searchInsurance() {
    printTitle("보험조회");
    List<Insurance> insuranceList = insuranceImpl.searchInsurance();
    printTable(insuranceList);
    AuthCommand.initialize();
  }

  public static void designInsurance() {
    printTitle("보험설계");
    InsuranceType insuranceType = insuranceParser.getInsuranceType();
    int count = insuranceImpl.getCountByInsuranceType(insuranceType);

    Insurance insurance = Insurance.builder()
        .insuranceType(insuranceType)
        .insuranceCode(insuranceType.name() + "-" + (count + 1))
        .name(insuranceParser.getName())
        .coverDescription(insuranceParser.getCoverDescription())
        .interestRate(insuranceParser.getInterestRate())
        .entryAge(insuranceParser.getEntryAge())
        .premium(insuranceParser.getPremium())
        .premiumRate(insuranceParser.getPremiumRate())
        .authorizeType(AuthorizeType.AUTHORIZE_WAITED)
        .build();
    insuranceImpl.designInsurance(insurance);
    System.out.println("보험설계가 완료되었습니다.");
    AuthCommand.initialize();
  }

  public static void requestInsurance() {
    printTitle("보험인가 요청");
    System.out.print("보험리스트 : ");
    List<Insurance> insuranceList = insuranceImpl.searchInsurance();
    printTable(insuranceList);
    System.out.println("인가 요청할 보험을 선택해주세요");
    int selectedMenu = input();
    Insurance insurance = insuranceList.get(selectedMenu);
    insuranceImpl.requestInsuranceApproval(insurance);
    System.out.println("선택한 상품에 대한 인가 요청이 완료되었습니다.");
    AuthCommand.initialize();
  }
}
