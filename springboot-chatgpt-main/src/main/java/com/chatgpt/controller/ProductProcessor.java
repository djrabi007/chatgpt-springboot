package com.chatgpt.controller;

import java.util.ArrayList;
import java.util.List;

public class ProductProcessor {
	private static final String FAILURE = "failure";
	private static final String SUCCESS = "success";
	private static final String Y = "Y";
	private static final String N = "N";
	private static final String RECORD_NOT_FOUND = "RECORD_NOT_FOUND";
	// private static final int MAX_RECORDS =25;
	private static final int BATCH_SIZE = 3;// 10

	public static void main(String[] args) {
		List<Product> products = populateProduct(); // Your list of products - MULE CALL
		List<Product> productsUpdated = new ArrayList<>();
		System.out.println("Before update product -->" + products);

		int totalRecords = products.size(); // 1000 records from TIBCO
		int processedRecords = 0;


		// Process records in batches
		while (processedRecords < totalRecords) {
			List<Product> batch = getRecordsFromDatabase(processedRecords, BATCH_SIZE, products);

			// Process each product in the batch
			for (Product product : batch) {
				if (product.getStatus().equals(N)) {
					// Invoke function m1
					boolean isSuccess;
					try {
						isSuccess = m1(product.getDescription());
						if (isSuccess) {
							product.setStatus(Y);
							productsUpdated.add(product); // Only update Success records
						}

					} catch (Exception e) {
						if (RECORD_NOT_FOUND.equals(e.getMessage())) {
							product.setStatus(Y);
							productsUpdated.add(product); // Only update Success records
						}
					}



				}

			}

			// Update the processed records count
			processedRecords += batch.size();
			if (BATCH_SIZE < totalRecords) {
				break;
			}

		}

		// Update the database with the modified products
		updateDatabase(productsUpdated);

		System.out.println("After  update product -->" + productsUpdated);
	}


	public static List<Product> populateProduct() {
		List<Product> records = new ArrayList<>();

		Product p1 = new Product(1, N, SUCCESS);
		Product p2 = new Product(2, N, SUCCESS);
		Product p3 = new Product(3, N, FAILURE);
		Product p4 = new Product(4, Y, RECORD_NOT_FOUND);// Y
		Product p5 = new Product(5, N, SUCCESS);
		Product p6 = new Product(6, Y, RECORD_NOT_FOUND);// Y
		Product p7 = new Product(7, N, SUCCESS);
		Product p8 = new Product(8, N, RECORD_NOT_FOUND);
		Product p9 = new Product(9, N, FAILURE);

		records.add(p1);
		records.add(p2);
		records.add(p3);
		records.add(p4);
		records.add(p5);
		records.add(p6);
		records.add(p7);
		records.add(p8);
		records.add(p9);
		return records;
	}

	public static List<Product> getRecordsFromDatabase(int offset, int limit, List<Product> records) {
		// Your implementation to retrieve records from the database
		// Implement the logic to fetch records starting from the offset with the given
		// limit
		List<Product> recordsFinal = new ArrayList<>();
		// Retrieve records from the database and add them to the 'records' list



		limit = limit + offset; // 1st 0-4 , 2nd-->4-8
		if (limit > records.size()) {
			limit = records.size();
		}

		// For Batch=6 , Mule= 250

//		else if (records.size() > limit) {
//			limit = limit;
//		}

		for (int i = offset; i < limit; i++) {
			recordsFinal.add(records.get(i));
		}

		return recordsFinal;
	}

	public static boolean m1(String desc) throws Exception {
		// Your implementation of function m1
		// Return true if successful, false otherwise
		boolean b=false;
		try {
			if (SUCCESS.equals(desc)) {
				b = true;
			}
			else if (RECORD_NOT_FOUND.equals(desc)) {
				throw new Exception(RECORD_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return b;
	}

	public static void updateDatabase(List<Product> products) {
		// Your implementation to update the database with the modified products
	}
}

class Product {
	private int olbId;
	private String status;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product(int olbId, String status,String description) {
		this.olbId = olbId;
		this.status = status;
		this.description = description;
	}

	public int getOlbId() {
		return olbId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Product [olbId=" + olbId + ", status=" + status + ", description=" + description + "]";
	}

}
