package command;

import command.menu.sales.ContractProgress;
import command.menu.sales.ContractManagement;
import command.menu.sales.CustomerManagement;
import command.menu.sales.Sales;
import command.menu.sales.SalesManagement;
import command.parser.SalesParser;
import domain.Contract;
import domain.Customer;
import domain.Employee;
import domain.Insurance;
import java.util.Arrays;
import java.util.List;
import service.impl.SalesServiceImpl;

public class SalesCommand extends Command {

  private static final SalesParser parser = SalesParser.getInstance();
  private static final SalesServiceImpl salesService = new SalesServiceImpl();

  public static void run() {
    executeCommand("영업", Sales.values());
  }

  public static void manageSales() {
    executeCommand("영업 관리", SalesManagement.values());
  }

  public static void manageInsurance() {
    printTitle("보험 관리");
    System.out.println("상세 조회할 보험을 선택해주세요.");
    String[] args = {"insuranceCode", "name", "insuranceType", "coverDescription"};
    List<Insurance> insuranceList = salesService.getInsuranceList();
    printTable(insuranceList, args);

    Insurance insurance = insuranceList.get(input());
    printTitle("보험 상세 정보");
    printTable(insurance);
    System.out.println("보험 정보 조회를 완료하였습니다.");

    AuthCommand.initialize();
  }

  public static void manageCustomer() {
    executeCommand("고객 관리", CustomerManagement.values());
  }

  public static void searchCustomer() {
    printTitle("고객 조회");
    System.out.println("상세 조회할 고객을 선택해주세요.");
    String[] args = {"name", "birth", "sex"};
    List<Customer> customerList = salesService.getCustomerList();
    printTable(customerList, args);

    Customer customer = customerList.get(input());
    printTitle("고객 정보 상세 조회");
    printTable(customer);
    System.out.println("고객 정보 조회를 완료하였습니다.");

    AuthCommand.initialize();
  }

  public static void signUpCustomer() {
    printTitle("신규 고객");
    System.out.println("신규 고객 정보를 입력해주세요.");
    Customer customer = Customer.builder()
        .accountNumber(parser.getAccountNumber())
        .address(parser.getAddress())
        .birth(parser.getBirth())
        .job(parser.getJob())
        .name(parser.getName())
        .phoneNumber(parser.getPhoneNumber())
        .sex(parser.getSex())
        .build();

    salesService.createCustomer(customer);
    System.out.println("신규 고객 가입이 완료되었습니다.");

    AuthCommand.initialize();
  }

  public static void manageOrganization() {
    printTitle("조직 관리");
    System.out.println("상세 정보를 조회할 사원을 선택해주세요.");
    List<Employee> employeeList = salesService.getEmployeeList();
    printTable(employeeList, "id", "name");

    Employee employee = employeeList.get(input());
    String[] args = {"id", "name", "email", "employeeType"};
    printTitle("사원 인적 정보");
    printTable(employee, args);
    System.out.println("사원 정보 조회를 완료하였습니다.");

    AuthCommand.initialize();
  }

  public static void manageContract() {
    executeCommand("계약 관리", ContractManagement.values());
  }

  public static void searchAllContract() {
    printTitle("모든 계약 조회");
    System.out.println("상세 정보를 조회할 계약을 선택해주세요.");
    String[] args = {"applicationDate", "customer", "insurance"};
    List<Contract> contractList = salesService.getContractList();
    printTable(contractList, args);

    Contract contract = contractList.get(input());
    printTitle("계약 상세 정보");
    printTable(contract);
    System.out.println("계약 정보 조회를 완료하였습니다.");

    AuthCommand.initialize();
  }

  public static void searchUnsignedContract() {
    printTitle("미체결 계약 리스트");
    System.out.println("상세 정보를 조회할 계약을 선택해주세요.");
    String[] args = {"applicationDate", "customer", "insurance"};
    List<Contract> unsignedContractList = salesService.getUnsignedContractList();
    printTable(unsignedContractList, args);

    Contract selectedContract = unsignedContractList.get(input());
    printTable(selectedContract);
    proceedContract(selectedContract);
    System.out.println("미체결 정보 조회를 완료하였습니다.");

    AuthCommand.initialize();
  }

  public static void proceedContract(Contract contract) {

    int selectedMenu = selectCommand("계약 진행", ContractProgress.values());
    Arrays.stream(ContractProgress.values()).forEach(menu -> {
      if (selectedMenu == menu.ordinal()) {
        menu.execute(contract);
      }
    });
  }

  public static void concludeContract(Contract contract) {
    printTitle("계약 체결");
    System.out.println("계약을 체결하시겠습니까?");
    if (selectYesOrNo()) {
      salesService.makeContractSigned(contract.getId());
      System.out.println("계약 체결이 완료되었습니다.");
      AuthCommand.initialize();
    } else {
      System.out.println("계약 체결을 취소합니다.");
      proceedContract(contract);
    }
  }

  public static void rejectContract(Contract contract) {
    printTitle("계약 거절");
    System.out.println("계약을 거절하시겠습니까?");
    if (selectYesOrNo()) {
      salesService.removeContract(contract.getId());
      System.out.println("계약 거절이 완료되었습니다.");
      AuthCommand.initialize();
    } else {
      System.out.println("계약 거절을 취소합니다.");
      proceedContract(contract);
    }
  }

  public static void makeContract() {

  }

  public static void managePayment() {

  }
}
