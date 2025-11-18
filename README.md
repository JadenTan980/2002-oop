# Development Roadmap

The system skeleton is in place, but every function still needs to be expanded to meet the full set of requirements. The roadmap below breaks the work into phased milestones. Within each phase, tackle the functions in the order listed so that later logic can rely on the earlier guarantees.

## Phase 1 – Core Entities & Data Integrity

**User & subclasses**
- `User#login`, `User#logout`, `User#changePassword`, `User#getUserID`, `User#getName`, `User#setName`, `User#getFilterPreferences`, `User#setFilterPreferences`: wire these to persistence and audit trails, enforce password policies, and make sure filter preferences survive menu switches.
- `Student#getYearOfStudy`, `Student#getMajor`, `Student#getApplications`, `Student#apply`, `Student#withdraw`, `Student#acceptPlacement`, `Student#hasAcceptedPlacement`: enforce year/major constraints, cap three concurrent applications, auto-withdraw conflicting submissions, surface application history even when postings are hidden.
- `CompanyRep#getCompanyName`, `CompanyRep#setCompanyName`, `CompanyRep#createInternship`, `CompanyRep#viewApplications`, `CompanyRep#toggleVisibility`, `CompanyRep#getInternships`, `CompanyRep#isApproved`, `CompanyRep#setApproved`: add limits (max five postings, max ten slots), gather full internship details, gate actions on approval, and expose filtered views of submitted opportunities and applications.
- `CareerCenterStaff#getStaffDepartment`, `CareerCenterStaff#setStaffDepartment`, `CareerCenterStaff#approveInternship`, `CareerCenterStaff#rejectInternship`, `CareerCenterStaff#approveRepAccount`, `CareerCenterStaff#processWithdrawal`: integrate these with controllers/managers so approvals persist, notifications dispatch, and withdrawal decisions update applications and slots appropriately.

**Domain value objects**
- `FilterCriteria` (constructor, `getStatus`, `setStatus`, `getPreferredMajor`, `setPreferredMajor`, `getLevel`, `setLevel`, `getClosingDate`, `setClosingDate`, `matches`): expand filtering to support saved preferences per user, alphabetical default ordering, and composite filters (status + major + level + closing date).
- `Internship` (constructor, getters/setters, `addSlot`, `getSlots`, `getApplications`, `addApplication`, `toggleVisibility`, `isVisible`, `isFull`): enforce slot cap, status transitions (Pending → Approved/Rejected/Filled), auto-close when dates lapse, and keep visibility toggles synced with student views.
- `InternshipSlot#getSlotNumber`, `InternshipSlot#getAssignedStudent`, `InternshipSlot#assignStudent`: connect slot assignment to placement acceptance and ability to mark opportunities as filled.
- `Application` (constructor, `getStudent`, `getInternship`, `getStatus`, `setStatus`, `getTimestamp`, `isWithdrawalRequested`, `markSuccessful`, `markUnsuccessful`, `requestWithdrawal`): persist state changes, log timeline events, and expose status to menus even after posting visibility changes.
- `WithdrawalRequest#getApplication`, `WithdrawalRequest#getStudent`, `WithdrawalRequest#getReason`, `WithdrawalRequest#getStatus`, `WithdrawalRequest#approve`, `WithdrawalRequest#reject`: capture staff decisions, reasons, and timestamps for reporting/compliance.
- `AccountRequest#getRep`, `AccountRequest#getStatus`, `AccountRequest#setStatus`, `AccountRequest#getApprover`, `AccountRequest#setApprover`: expand to track submission times and rejection reasons so staff can manage approvals effectively.

## Phase 2 – User Loading & Registration Workflow

**UserManager**
- `UserManager#loadAllUsers`, `#loadStudents`, `#loadStaff`, `#loadCompanyRepresentatives`: harden CSV ingestion (validate ID formats, trim headers, guard against duplicates) and support additional metadata columns (emails, phone numbers, default majors/levels).
- `UserManager#login`: add throttling, password hashing, and audit logs. Provide clearer error feedback (invalid ID vs wrong password vs pending approval).
- `UserManager#getLastLoginMessage`: extend to return actionable hints for the CLI and future UI clients.
- `UserManager#registerStudent`, `#registerCompanyRep`, `#registerCareerCenterStaff`: validate IDs per spec, hash passwords, and trigger welcome/approval notifications.
- `UserManager#approveRepresentative`: update account + request objects atomically, log approver/time, and expose rejection path.
- `UserManager#getPendingAccounts`: add pagination/filtering; ensure list stays consistent with persisted storage.
- Private helpers `loadStudents`, `loadStaff`, `loadCompanyRepresentatives`, `addUser`, `userExists`, `isReadable`, `parseInt`: flesh out error reporting, move parsing to reusable utilities, and prep for future database integration.

**App lifecycle**
- `App#App()` / `App#loadInitialUsers`: make the file paths configurable, handle missing files gracefully, and support refresh/reload commands.
- `App#start`: replace the placeholder loop with a structured menu router that can navigate to student/rep/staff portals and return cleanly.
- `App#promptLogin`: mask passwords, support password change/reset flows, and allow retries.
- `App#routeUser`: redirect to fully featured menus (student internship browsing, rep dashboard, staff console) once those are implemented.
- `App#handleRegistration`, `#registerStudent`, `#registerCompanyRep`, `#registerCareerCenterStaff`: add field validation, confirmation prompts, and persistence for new accounts. Ensure rep registration clearly communicates approval status.
- `App#readInt`: generalize into a reusable input utility (with default values, cancel paths, and error messaging).

## Phase 3 – Internship & Application Processing

**InternshipManager**
- `submitInternship`, `approveInternship`, `rejectInternship`, `filter`, `getInternships`: persist opportunities, enforce max-five-per-rep check, manage status transitions (Pending → Approved/Rejected/Filled), and expose filtered lists to students/company reps/career staff.

**ApplicationManager**
- `submitApplication`: integrate with notifications, enforce rule failures with friendly messages, and log submission timestamps.
- `updateStatus`: propagate status changes to students and reps, and auto-fill slots when approvals arrive.
- `enforceRules`: extend with additional requirements (visibility even after closing if student already applied, ability to view statuses, etc.) and extract messages describing which rule blocked the user.

**WithdrawalManager**
- `submitRequest`: capture reasons, timestamps, and ensure duplicate requests are prevented.
- `processRequest`: let staff approve or reject based on workload, update applications, and free up slots when withdrawals are accepted.

**Student / CompanyRep interactions**
- `Student#apply`, `#withdraw`, `#acceptPlacement`: call into ApplicationManager/WithdrawalManager, surface rule failures, and keep application list synced.
- `CompanyRep#createInternship`, `#viewApplications`, `#toggleVisibility`: use InternshipManager to create/update opportunities, enforce slot and visibility rules, and show application details with student info.

## Phase 4 – Staff Tools, Reporting, and UI Menus

**Career center workflows**
- `CareerCenterStaff#approveInternship`, `#rejectInternship`, `#approveRepAccount`, `#processWithdrawal`: replace direct field mutations with manager calls, ensuring audit logs and notifications fire.
- `WithdrawalManager#processRequest`, `ApplicationManager#updateStatus`: coordinate with staff decisions to keep records consistent.

**Reporting**
- `ReportGenerator#generateByStatus`, `#generateByMajor`, `#generateByLevel`, `#generateCompanySummary`: build comprehensive reports (counts, listings, filters by time window/company/major) and format them for CLI + future export.

**CLI menus**
- `App#showStudentMenu`: implement navigation for browsing/filtering internships, submitting applications, viewing statuses, requesting withdrawals, and accepting offers.
- `App#showRepMenu`: allow reps to submit postings, review applications, approve/reject candidates, toggle visibility, and monitor slot fulfillment.
- `App#showStaffMenu`: list pending rep accounts, internship submissions, withdrawal requests, and provide access to the reporting suite.

By following these phases in order—first strengthening the core entities, then user management, then application workflows, and finally the UI/reporting layers—you can iteratively turn the current skeleton into the fully featured internship management platform described in the requirements. Track progress by checking off each function as you implement its behavior.
