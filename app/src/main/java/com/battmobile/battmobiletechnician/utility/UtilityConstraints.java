package com.battmobile.battmobiletechnician.utility;

/**
 * Created by rahul on 11/01/2020.
 */
public class UtilityConstraints {

    public static class UrlLocation {
        public static String BASE_URL = "http://mindsmetricksdemo.com/MB/web/";

        public static String LOGIN = BASE_URL + "users/api/login";
        public static String FORGOT_PASSWORD = BASE_URL + "users/api/forgot-password";


        public static String JOBS_LIST = BASE_URL + "jobs/api/assigned-job-list";
        public static String JOB_DETAIL = BASE_URL + "jobs/api/view";
        public static String PROCESS_JOB = BASE_URL + "jobs/api/process-job";
        public static String RESCHEDULE_JOB = BASE_URL + "jobs/api/request-rescheduling";
        public static String REQUEST_OVERTIME = BASE_URL + "overtimes/api/request";


        public static String CASH_IN_HAND_LIST = BASE_URL + "cash-in-hand/api/list";
        public static String TRANSFER_LIST = BASE_URL + "cash-in-hand/api/transferred-cash";
        public static String RECEIVE_LIST = BASE_URL + "cash-in-hand/api/received-cash";

        public static String EXPENSE_LIST = BASE_URL + "expenses/api/list";
        public static String ADD_EXPENSE = BASE_URL + "expenses/api/add";
        public static String EDIT_EXPENSE = BASE_URL + "expenses/api/edit";


        public static String OVERTIME_LIST = BASE_URL + "overtimes/api/list";
        public static String OVERTIME_COLLECTION = BASE_URL + "overtimes/api/overtime-collection";


        public static String INCENTIVE_LIST = BASE_URL + "incentives/api/list";


        public static String SEARCH_BRAND_LIST = BASE_URL + "brands/api/brand-search";
        public static String SEARCH_PRODUCT_LIST = BASE_URL + "products/api/product-search";


        public static String ADD_SCRAP_PURCHASED_BATTERY = BASE_URL + "stock/api/add-battery-by-technician";

        public static String VEHICLE_LIST = BASE_URL + "company-vehicles/api/list";


        public static String INSURANCE_LIST = BASE_URL + "company-vehicles/api/get-insurance-history";
        public static String ADD_INSURANCE = BASE_URL + "company-vehicles/api/add-insurance-history";
        public static String EDIT_INSURANCE = BASE_URL + "company-vehicles/api/edit-insurance-history";


        public static String SERVICE_LIST = BASE_URL + "company-vehicles/api/get-service-history";
        public static String ADD_SERVICE = BASE_URL + "company-vehicles/api/add-service-history";
        public static String EDIT_SERVICE = BASE_URL + "company-vehicles/api/edit-service-history";


        public static String ODOMETER_LIST = BASE_URL + "company-vehicles/api/get-odometer-reading";
        public static String ADD_ODOMETER = BASE_URL + "company-vehicles/api/add-odometer-reading";
        public static String EDIT_ODOMETER = BASE_URL + "company-vehicles/api/edit-odometer-reading";


        public static String REFERRAL_LIST = BASE_URL + "referrals/api/list";
        public static String ADD_REFERRAL = BASE_URL + "referrals/api/add";
        public static String EDIT_REFERRAL = BASE_URL + "referrals/api/edit";


        public static String SOLD_BATTERIES_LIST = BASE_URL + "stock/api/get-sold-scrap";
        public static String ADD_SOLD_BATTERIES = BASE_URL + "stock/api/sell-scrap";


        public static String ASSIGNED_LIST = BASE_URL + "stock/api/get-allocated-stock";

        public static String STOCK_LIST = BASE_URL + "stock/api/list";
        public static String BRAND_LIST = BASE_URL + "brands/api/list";
        public static String GET_TECHNICIAN = BASE_URL + "users/api/users-list";
        public static String GET_SUPPLIER = BASE_URL + "suppliers/api/list";
        public static String ADD_STOCK = BASE_URL + "stock/api/add";
        public static String EDIT_STOCK = BASE_URL + "stock/api/edit";
        public static String ALLOCATE_STOCK = BASE_URL + "stock/api/allocate-stock";
        public static String SELL_SCRAP = BASE_URL + "stock/api/sell-scrap";
        public static String UPDATE_PROFILE = BASE_URL + "users/api/edit-user";
        public static String CHANGE_PASSWORD = BASE_URL + "users/api/change-password";
        public static String CASH_TRANSFER = BASE_URL + "cash-in-hand/api/transfer-cash";
    }

    public static class StatusConstraints {
        public static String REASON_FOROGOT_PASSWORD = "Forgot Password";
        public static String REASON_REGISTER = "Register";
        public static String EMAIL = "email";
        public static String USERNAME = "username";
        public static String PASSWORD = "password";
        public static String ANDROID = "Android";
    }

    public static class Value {
        public static String EMAIL = "email";
        public static String MESSAGE = "message";
        public static String STATUS = "status";
        public static String RESPONSE = "response";
        public static String FIRST_NAME = "first_name";
        public static String LAST_NAME = "last_name";
        public static String ID_USER = "id_user";
        public static String LOCATION = "location";
        public static String GENDER = "gender";
        public static String DOB = "dob";
        public static String LANGUAGE = "native_language";
        public static String COMPLETED = "completed";
        public static String PROFESSION = "profession";


    }

    public static class FixedPath {

        public static String QR_PATH = "/Android/data/io.EasyConnect.EasyConnect/files/EasyConnectQR.jpg";
        public static String PROFILE_PATH = "/Android/data/io.EasyConnect.EasyConnect/files/EasyConnectProfile.jpg";
        public static String USER_MAIL = "@easyconnect.io";
        public static String USER_PASSWORD = "Ceo@easyconnect.io";
    }

    public static class Messages {
        public static String SESSION_SUCCESS = "Session Succefully created";
        public static String SESSION_BACK = "Session is Playing. Stop to Proceed";
        public static String SESSION_CREATE_NEW = "Session is Playing. Stop to create new";
        public static String SESSION_STOP = "Session Stoped ";
        public static String SESSION_START = "Session Started ";
        public static String SESSION_PAUSE = "Session Paused ";
        public static String SESSION_RESUME = "Session Resumed ";
        public static String SESSION_CREATE = "Create Session to use this feature";
        public static String SESSION_ALREADY = "Session already created.This will replace previous session. ";
        public static String SESSION_CREATE_AGAIN = "Do you want to create again?";
        public static String HEAL_MY_LIFE = "HealMyLife";
        public static String FORGOT_PASSWORD_TITLE = "Forgot Password";
        public static String LOADING = "Loading....";
        public static String DONE = "Done";
        public static String LEAVE = "Leave";
        public static String PERMISSION_GRANTED = "Permission Granted";
        public static String PERMISSION_ALLOW = "Please allow permission Setting-> App -> Healmylife->Permission";
        public static String SAVE_SESSION = "You want to save this session";
        public static String YES = "Yes";
        public static String NO = "No";
        public static String LOGOUT_CONFIRM = "Are you sure you want to Logout ?";
        public static String LOGOUT = "Logout";
        public static String LOGOUT_SUCCESS = "Logout Success";
        public static String LOGOUT_ISSUE = "Logout with some issue";
        public static String DELETE_CONFIRM = "Are you sure you want to delete your account ?";
        public static String DOB = "Date of Birth";
        public static String MALE = "Male";
        public static String FEMALE = "Female";
        public static String OTHER = "Other";
        public static String DELETE_SUCCESS = "Account Successfully deleted";
        public static String FORGOT_PASSWORD_MESSAGE = "Enter Email ID";
        public static String ERROR_CONNECTION = "Please Check Network Connection!";
        public static String ERROR_NEW_PASSWORD = "Invalid new password";
        public static String ERROR_CONFIRM_PASSWORD = "Invalid confirm password";
        public static String ERROR_MOBILE_NO = "Please enter 10 digit mobile no.";
        public static String ERROR_EMPTY_VERIFICATION_CODE = "Please enter verification code.";
        public static String ERROR_VERIFICATION_CODE_MISMATCH = "Verification code mismatch.Please enter valid verification code.";
        public static String PLEASE_ADD_PRODUCT_QUANTITY = "Please add product quantity!";
        public static String ITEM_ADDED_TO_CART = "Item added to cart";
        public static String SUCCESSFULLY_SIGNUP = "Account Created Successfully";
        public static String ERROR_EMPTY_FULLNAME = "Please enter full name.";
        public static String ERROR_EMPTY_EMAIL_ID = "Please enter Email ID";
        public static String ERROR_EMPTY_OTP = "Please enter OTP";
        public static String ERROR_EMPTY_USERNAME = "Please enter Valid Username";
        public static String ERROR_EMPTY_ADDRESS = "Please enter address";
        public static String ERROR_INVALID_EMAIL_ID = "Email id invalid";
        public static String ERROR_INVALID_OTP = "OTP is invalid";
        public static String ERROR_SELECT_CITY = "Please select city.";
        public static String ERROR_SELECT_STATE = "Please select state.";
        public static String ERROR_EMPTY_PINCODE = "Please enter pincode.";
        public static String ERROR_EMPTY_COUPON_CODE = "Please enter coupon code.";
        public static String ERROR_EMPTY_ADDRESS_LINE_ONE = "Please enter address line one.";
        public static String SUCCESS_UPDATE_ADDRESS_STATUS = "Address status update successfully.";
        public static String SUCCESS_DELETE_ADDRESS = "Address delete successfully.";
        public static String SUCCESS_ITEM_WISHLISTED = "Item wishlisted";
        public static String EMPTY_PRODUCT_LIST = "OrderModel list empty";
        public static String EMPTY_WISHLIST_PRODUCT_LIST = "Item is not wishlisted.";
        public static String EMPTY_OFFER_LIST = "Offers not available.";
        public static String EMPTY_CARYT = "No item added in cart";
        public static String EMPTY_ORDER = "Order list empty";
        public static String PRODUCT_ALREADY_LIKED = "OrderModel already liked";
        public static String SIGN_UP_FAIL = "Registration Failed";
        public static String SOMETHING_IS_WRONG = "Something is wrong";
        public static String EMPTY_MOBILE_NO = "Mobile no can not be empty";
    }

    public static class CustomToast {

        public static String INVALID_EMAIL = "Invalid Email ";
        public static String INVALID_USER = "Minimum 5 Character requried";
        public static String INVALID_PASSWORD = "Minimum 6 Characters requried";
        public static String PASSWORD_MAX_LENGTH = "Maximum 15 Characters required";
        public static String INVALID_FIRST_NAME = "Invalid First Name";
        public static String INVALID_LAST_NAME = "Invalid Last Name";
        public static String PASSWORD_RESET = "Password reset successfully";
        public static String EMAIL_EMPTY = "Email can not be empty";
        public static String USER_EMPTY = "Username can not be empty";
        public static String PASSWORD_EMPTY = "Password can not be empty";
        public static String FIRST_EMPTY = "First Name can not be empty";
        public static String LAST_EMPTY = "Last Name can not be empty";
        public static String PROFESSION_EMPTY = "Profession Empty";
        public static String LOCATION_EMPTY = "Location Empty";
        public static String LANGUAGE_EMPTY = "Select Native Language";
        public static String DOB_EMPTY = "Select Date of Birth";
        public static String PROBLEM_EMPTY = "Select Health Problem";
        public static String EMPTY_NAME = "Name can not be empty";
        public static String INVALID_NAME = "Invalid name";
        public static String INVALID_MOBILE_NO = "Enter 10 digit mobile Numbe";
        public static String PERMISSION_DENIED = "Permission denied, can't emable the camera";
    }


    public static class Constraints {

        public static class PartnerType {
            public static String ECONOMY_STORE = "2";
            public static String AUTOJAGAT_STORE = "3";
            public static String BRAND_AUTHORIZED_STORE = "1";
        }

        public static class UsedVehicle {
            public static class SortInput {
                public static String DEFAULT = "";
                public static String SKU = "sku";
                public static String BRAND_NAME = "brand_name";
            }

            public static class SortType {
                public static String DEFAULT = "";
                public static String AZ = "a_z";
                public static String ZA = "z_a";
            }

        }

        public static class VehicleType {
            public static String CAR = "1";
            public static String BIKE = "2";
        }

        public static class EnquiryType {
            public static String LOAN = "1";
            public static String INSURANCE = "2";
            public static String CONTACT = "4";
            public static String BUY_USED_VEHICLE = "6";
            public static String SELL_USED_VEHICLE = "7";
            public static String ROADSIDE = "10";
        }

        public static class SocialPage {
            public static String FACEBOOK = "https://www.facebook.com/autojagat";
            public static String FACEBOOK_ID = "autojagat";
            public static String TWITTER = "https://twitter.com/autojagat";
            public static String INSTAGRAM = "https://www.instagram.com/autojagat/";
            public static String LINKEDIN = "https://www.linkedin.com/company/autojagat";
        }

        public static class ContactDetail {
            public static String EMAIL = "care@autojagat.com";
            public static String FAQ = "https://www.autojagat.com/faq";
            public static String REFER_TC = "https://www.autojagat.com/policy/refer-term-conditions";
            public static String CONTACT_NUMBER = "18001219646";
        }

    }
}
