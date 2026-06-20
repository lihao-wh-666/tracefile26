/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './cms/src/main/webapp/**/*.jsp',
    './cms/src/main/webapp/**/*.html',
    './cms/src/main/webapp/static/js/**/*.js',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#4f6ef7',
          light: '#7b93fa',
          lighter: '#eef1fe',
          dark: '#3a54d4',
        },
        success: {
          DEFAULT: '#36b37e',
          light: '#57d9a3',
          lighter: '#e3fcef',
          dark: '#2d9969',
        },
        warning: {
          DEFAULT: '#f5a623',
          light: '#f7c948',
          lighter: '#fffae6',
          dark: '#e0911a',
        },
        danger: {
          DEFAULT: '#ef5350',
          light: '#f87171',
          lighter: '#fdeaec',
          dark: '#d32f2f',
        },
        info: {
          DEFAULT: '#00b8d9',
          lighter: '#e6fcff',
        },
        text: {
          primary: '#172b4d',
          secondary: '#5e6c84',
          tertiary: '#97a0af',
          disabled: '#b3bac5',
        },
        bg: {
          base: '#f4f5f7',
          elevated: '#ffffff',
          sunken: '#ebecf0',
        },
        border: {
          DEFAULT: '#dfe1e6',
          light: '#ebecf0',
        },
        sidebar: {
          start: '#0a1628',
          end: '#111d35',
        },
      },
      fontFamily: {
        sans: [
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          '"PingFang SC"',
          '"Hiragino Sans GB"',
          '"Microsoft YaHei"',
          '"Helvetica Neue"',
          'Arial',
          'sans-serif',
        ],
      },
      boxShadow: {
        'xs': '0 1px 2px rgba(23, 43, 77, 0.06)',
        'sm': '0 1px 3px rgba(23, 43, 77, 0.08), 0 1px 2px rgba(23, 43, 77, 0.04)',
        'md': '0 4px 6px -1px rgba(23, 43, 77, 0.08), 0 2px 4px -1px rgba(23, 43, 77, 0.04)',
        'lg': '0 10px 15px -3px rgba(23, 43, 77, 0.08), 0 4px 6px -2px rgba(23, 43, 77, 0.04)',
        'xl': '0 20px 25px -5px rgba(23, 43, 77, 0.08), 0 10px 10px -5px rgba(23, 43, 77, 0.04)',
        'primary': '0 2px 4px rgba(79, 110, 247, 0.3)',
        'primary-lg': '0 4px 8px rgba(79, 110, 247, 0.35)',
        'success': '0 2px 4px rgba(54, 179, 126, 0.3)',
        'success-lg': '0 4px 8px rgba(54, 179, 126, 0.35)',
        'warning': '0 2px 4px rgba(245, 166, 35, 0.3)',
        'warning-lg': '0 4px 8px rgba(245, 166, 35, 0.35)',
        'danger': '0 2px 4px rgba(239, 83, 80, 0.3)',
        'danger-lg': '0 4px 8px rgba(239, 83, 80, 0.35)',
        'pagination-primary': '0 2px 4px rgba(79, 110, 247, 0.25)',
        'sidebar': '0 2px 8px rgba(79, 110, 247, 0.35)',
        'login': '0 20px 25px -5px rgba(23, 43, 77, 0.08), 0 10px 10px -5px rgba(23, 43, 77, 0.04), 0 0 0 1px rgba(255, 255, 255, 0.1)',
        'focus-primary': '0 0 0 3px rgba(79, 110, 247, 0.15)',
        'focus-danger': '0 0 0 3px rgba(239, 83, 80, 0.1)',
        'header': '0 1px 3px rgba(23, 43, 77, 0.05), 0 1px 2px rgba(23, 43, 77, 0.03)',
      },
      borderRadius: {
        'sm': '4px',
        'md': '8px',
        'lg': '12px',
        'xl': '16px',
      },
      spacing: {
        'sidebar': '240px',
        'header': '64px',
      },
      transitionTimingFunction: {
        'fast': 'cubic-bezier(0.4, 0, 0.2, 1)',
        'normal': 'cubic-bezier(0.4, 0, 0.2, 1)',
        'slow': 'cubic-bezier(0.4, 0, 0.2, 1)',
      },
      transitionDuration: {
        'fast': '150ms',
        'normal': '250ms',
        'slow': '350ms',
      },
      keyframes: {
        'fade-in-up': {
          'from': {
            opacity: '0',
            transform: 'translateY(12px)',
          },
          'to': {
            opacity: '1',
            transform: 'translateY(0)',
          },
        },
        'login-box-in': {
          'from': {
            opacity: '0',
            transform: 'translateY(30px) scale(0.96)',
          },
          'to': {
            opacity: '1',
            transform: 'translateY(0) scale(1)',
          },
        },
        'alert-slide-in': {
          'from': {
            opacity: '0',
            transform: 'translateY(-8px)',
          },
          'to': {
            opacity: '1',
            transform: 'translateY(0)',
          },
        },
        'login-bg-float': {
          '0%, 100%': { transform: 'translate(0, 0) rotate(0deg)' },
          '33%': { transform: 'translate(2%, -1%) rotate(1deg)' },
          '66%': { transform: 'translate(-1%, 1%) rotate(-0.5deg)' },
        },
      },
      animation: {
        'fade-in-up': 'fadeInUp 0.4s ease both',
        'login-box-in': 'loginBoxIn 0.6s cubic-bezier(0.34, 1.56, 0.64, 1) both',
        'alert-slide-in': 'alertSlideIn 0.35s cubic-bezier(0.34, 1.56, 0.64, 1) both',
        'login-bg-float': 'loginBgFloat 20s ease-in-out infinite',
      },
    },
  },
  plugins: [],
}
