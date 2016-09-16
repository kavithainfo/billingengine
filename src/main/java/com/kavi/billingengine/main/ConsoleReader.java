package com.kavi.billingengine.main;

import com.kavi.billingengine.domain.BillingRequest;
import com.kavi.billingengine.domain.ServiceType;

import java.util.Scanner;

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;

public class ConsoleReader {

    private final BillingRequestConverter billingRequestConverter;

    public ConsoleReader(BillingRequestConverter billingRequestConverter) {
        this.billingRequestConverter = billingRequestConverter;
    }

    public BillingRequest read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to MediHealth...");

        System.out.println("Please enter your age: ");

        while(scanner.hasNext()) {
            // age input
            final String ageString = scanner.next();
            Integer age = 1;
            try{
                Integer.valueOf(ageString);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect age format. Please enter your age in this format: 45 ");
                continue;
            }

            // service input
            System.out.println("Please enter the service you would like today from the following options: ");
            System.out.println(asList(ServiceType.values()));

            final String service = scanner.next();
            ServiceType serviceType = null;
            try{
                serviceType = ServiceType.valueOf(service);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid service, Please enter as from the list");
                continue;
            }

            System.out.println("Please enter the quantity of the service " + serviceType + ": ");

            final String quantityString = scanner.next();
            Integer quantity = 1;
            try{
                quantity = Integer.valueOf(quantityString);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid quantity, Please enter a number: ");
                continue;
            }

            System.out.println("Do you have a MediHealth insurance? [true/false]");
            final String insurance = scanner.next();
            Boolean hasInsurance = FALSE;
            try{
                hasInsurance = Boolean.valueOf(insurance);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input, Please enter true/false ");
                continue;
            }

            System.out.println("Have you been diagnosed by a MediHealth Practitioner? [true/false]");
            final String diagnosed = scanner.next();
            Boolean hasBeenDiagnosed = FALSE;
            try{
                hasBeenDiagnosed = Boolean.valueOf(diagnosed);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input, Please enter true/false ");
                continue;
            }

            return billingRequestConverter.convert(serviceType, age, hasInsurance, hasBeenDiagnosed);
        }
        return null;
    }
}
